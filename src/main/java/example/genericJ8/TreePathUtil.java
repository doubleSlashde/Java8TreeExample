package example.genericJ8;

import static java.util.stream.Collectors.joining;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import example.TreeNode;

public class TreePathUtil {

    public static <T> String getTreePath(TreeNode<T> treeNode, Function<T, String> extractorFct, String separator) {
        List<String> path = new ArrayList<>();
        getTreePath(path, treeNode, extractorFct);
        return path.stream().collect(joining(separator));
    }

    private static <T, U> void getTreePath(List<U> path, TreeNode<T> node, Function<T, U> extractorFct) {
        if (node != null && node.getData() != null) {
            path.add(0, extractorFct.apply(node.getData()));
            if (node.getParent() != null) {
                getTreePath(path, node.getParent(), extractorFct);
            }
        }
    }

}
