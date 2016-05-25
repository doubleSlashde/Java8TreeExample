package example.genericJ8;
import java.util.function.Function;

import org.apache.commons.lang3.StringUtils;

import example.TreeNode;

public class TreeUtil_2_GenericJava8 {

    public static <T> String printTree(TreeNode<T> treeNode, Function<TreeNode<T>, String> attrExtr) {
        StringBuilder sb = new StringBuilder();
        printTree(treeNode, sb, 0, attrExtr);
        return sb.toString();
    }

    private static <T> void printTree(TreeNode<T> treeNode, StringBuilder sb, int indent, Function<TreeNode<T>, String> extractorFct) {
        if (treeNode != null) {
            sb.append(StringUtils.repeat("  ", indent) + extractorFct.apply(treeNode));
            if (treeNode.getChildren() != null) {
                for (TreeNode<T> data : treeNode.getChildren()) {
                    printTree(data, sb, indent + 1, extractorFct);
                }
            }
        }
    }
    
}
