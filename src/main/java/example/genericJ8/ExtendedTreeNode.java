package example.genericJ8;

import java.util.stream.Stream;

import example.TreeNode;

public class ExtendedTreeNode<T> extends TreeNode<T> {

    public ExtendedTreeNode(TreeNode<T> node) {
        super(node);
    }

    public Stream<ExtendedTreeNode<T>> flattened() {
        return Stream.concat(Stream.of(this),
                getChildren().stream()
                        // extend
                        .map(ExtendedTreeNode<T>::new)
                        // recurse
                        .flatMap(ExtendedTreeNode<T>::flattened));
    }

}
