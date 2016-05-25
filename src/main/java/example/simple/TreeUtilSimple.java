package example.simple;
import org.apache.commons.lang3.StringUtils;

import example.ExampleData;
import example.TreeNode;

public class TreeUtilSimple {

    public static String printTree(TreeNode<ExampleData> treeNode) {
        StringBuilder sb = new StringBuilder();
        printTree(treeNode, sb, 0);
        return sb.toString();
    }
    
    private static void printTree(TreeNode<ExampleData> treeNode, StringBuilder sb, int indent) {
        if (treeNode != null) {
            sb.append(StringUtils.repeat("  ",  indent) + treeNode.getData().getName() + "\n");
            if (treeNode.getChildren() != null) {
                for (TreeNode<ExampleData> data : treeNode.getChildren()) {
                    printTree(data, sb, indent+1); 
                }
            }
        }
    }
    
}
