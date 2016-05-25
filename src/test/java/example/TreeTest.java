package example;

import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.rightPad;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;

import example.genericJ8.ExtendedTreeNode;
import example.genericJ8.TreePathUtil;
import example.genericJ8.TreeUtil_2_GenericJava8;
import example.simple.TreeUtilSimple;

public class TreeTest {

    @Test
    public void test() {

        // ----------------
        // Build tree
        // ----------------

        TreeNode<ExampleData> tree = new TreeNode<ExampleData>(new ExampleData("root", 0));
        tree.addChild(new ExampleData("plants", 1));

        tree.getChildren().get(0).addChild(new ExampleData("tree", 548));
        tree.getChildren().get(0).getChildren().get(0).addChild(new ExampleData("birch tree", 123));
        tree.getChildren().get(0).getChildren().get(0).addChild(new ExampleData("oak tree", 5));

        tree.getChildren().get(0).addChild(new ExampleData("flower", 567));
        tree.getChildren().get(0).getChildren().get(1).addChild(new ExampleData("sun flower", 3));
        tree.getChildren().get(0).getChildren().get(1).addChild(new ExampleData("amaryllis", 9));
        tree.getChildren().get(0).getChildren().get(1).addChild(new ExampleData("begonia", 2));

        tree.addChild(new ExampleData("animals", 2));
        tree.getChildren().get(1).addChild(new ExampleData("lion", 345));
        tree.getChildren().get(1).addChild(new ExampleData("monkey", 239));

        // ----------------
        // Print tree
        // ----------------

        System.out.println("------------------------------------------------");
        System.out.println("-- simple version");
        System.out.println("------------------------------------------------");
        System.out.println(TreeUtilSimple.printTree(tree));

        // Ok, but what if I want another attribute than name or several attributes?
        // The simple version cannot easily be converted to a generic one,
        // because it has hard access to ExampleData#getName().
        // Furthermore it has hard coded system dependent line break "\n".

        System.out.println("------------------------------------------------");
        System.out.println("-- advanced version with Generics and Java 8");
        System.out.println("------------------------------------------------");

        // output tree using name attribute
        System.out.println(TreeUtil_2_GenericJava8.printTree(tree, d -> String.valueOf(d.getData().getName()) + "\n"));

        // output tree using num attribute
        System.out.println(TreeUtil_2_GenericJava8.printTree(tree, d -> String.valueOf(d.getData().getNum()) + "\n"));

        System.out.println("------------------------------------------------");
        System.out.println("-- advanced version with tree path");
        System.out.println("------------------------------------------------");

        System.out.println(TreeUtil_2_GenericJava8.printTree(tree,
                d -> rightPad(d.getData().getName(), 20) //
                        + rightPad(String.valueOf(d.getData().getNum()), 20) //
                        + rightPad(String.valueOf(d.getData().getFoo()), 20) //
                        + TreePathUtil.getTreePath(d, ExampleData::getName, " / ") + "\n"));

        System.out.println("------------------------------------------------");
        System.out.println("-- some asserts");
        System.out.println("------------------------------------------------");

        // Passing extractor functions around helps also in testing:
        // We can easily sum over any numeric attribute.
        // Stream API helps to easily select and filter tree.

        // Some asserts summing attributes
        List<TreeNode<ExampleData>> level1 = nextLevelStream(tree).collect(Collectors.toList());
        assertEquals(new BigDecimal("3"), calculateSum(level1, d -> BigDecimal.valueOf(d.getData().getNum())));
        assertEquals(new BigDecimal("30"), calculateSum(level1, d -> d.getData().getFoo()));

        // Assert counting on one level of the tree
        System.out.println();
        assertEquals(4,
                level2Stream(tree)
                        // peek for debug output...
                        .peek(printName()) //
                        .count());

        // Asserting that there are only the given values as names on level 2
        System.out.println();
        assertAllowedVals(
                level2Stream(tree)
                        // peek for debug only
                        .peek(printName()) //
                        .collect(Collectors.toList()),
                n -> n.getData().getName(),
                // allowed names on level 2
                "tree", "flower", "lion", "monkey");

        // same but letting it fail for demo purpose
        System.out.println();
        try {
            assertAllowedVals(
                    level2Stream(tree)
                            // peek for debug only
                            .peek(printName()) //
                            .collect(Collectors.toList()),
                    n -> n.getData().getName(),
                    // allowed names on level 2
                    "tree", "flower", "foo", "bar");
        } catch (AssertionError e) {
            // ignore
            System.out.println(e.getMessage());
        }

        // Assert counting on one level of the tree
        System.out.println();
        assertEquals(5,
                level3Stream(tree)
                        // peek for debug output...
                        .peek(printName()) //
                        .count());

        System.out.println("\nall values in the tree:");
        flattenedStream(tree).map(ExampleData::getName).forEach(System.out::println);

        assertEquals(new BigDecimal("1844"),
                calculateSum(
                        flattenedStream(tree)
                                // for debug only
                                .peek(r -> System.out.println(r.getNum())) //
                                // collect
                                .collect(toList()),
                // extract (method reference better than lambda because more explicit, type is visible)
                ExampleData::getNumAsBigDecimal));

    }

    private Stream<ExampleData> flattenedStream(TreeNode<ExampleData> tree) {
        return new ExtendedTreeNode<ExampleData>(tree).flattened().map(TreeNode::getData);
    }

    private Consumer<? super TreeNode<ExampleData>> printName() {
        return n -> System.out.println(n.getData().getName());
    }

    private Stream<TreeNode<ExampleData>> level3Stream(TreeNode<ExampleData> tree) {
        return level2Stream(tree).flatMap(l2 -> nextLevelStream(l2));
    }

    private Stream<TreeNode<ExampleData>> level2Stream(TreeNode<ExampleData> tree) {
        return nextLevelStream(tree).flatMap(l1 -> nextLevelStream(l1));
    }

    private Stream<TreeNode<ExampleData>> nextLevelStream(TreeNode<ExampleData> tree) {
        return tree.getChildren().stream();
    }

    public static <T> BigDecimal calculateSum(final List<T> result,
            final Function<? super T, BigDecimal> colExtracter) {
        return result.stream().map(colExtracter)
                // Ignore null values
                .filter(col -> col != null)
                // Summing BigDecimal
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public <T> void assertAllowedVals(final List<T> result, final Function<? super T, ? extends String> colExtracter,
            final String... allowedVals) {
        // Value must match any of the allowed values
        result.stream().map(colExtracter)
                .forEach(value -> assertTrue(String.format("value \"%s\" does not match allowed values", value),
                        Stream.of(allowedVals).anyMatch(allowed -> allowed.equals(value))));
    }

}
