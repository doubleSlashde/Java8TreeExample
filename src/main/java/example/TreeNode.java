package example;
import java.util.LinkedList;
import java.util.List;

public class TreeNode<T> {

    T data;
    TreeNode<T> parent;
    List<TreeNode<T>> children;
    
    public List<TreeNode<T>> getChildren() {
        return children;
    }
    
    public TreeNode<T> getParent() {
        return parent;
    }
    
    public T getData() {
        return data;
    }

    public TreeNode(T data) {
        this.data = data;
        this.children = new LinkedList<TreeNode<T>>();
    }
    
    public TreeNode(TreeNode<T> n) {
        this.data = n.getData();
        this.children = n.getChildren();
    }

    public TreeNode<T> addChild(T child) {
        TreeNode<T> childNode = new TreeNode<T>(child);
        childNode.parent = this;
        this.children.add(childNode);
        return childNode;
    }

}
