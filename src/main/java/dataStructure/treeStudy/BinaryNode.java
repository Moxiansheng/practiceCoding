package dataStructure.treeStudy;

public class BinaryNode {
    private Integer val;
    private BinaryNode left;
    private BinaryNode right;

    public BinaryNode(Integer val){
        this.val = val;
    }

    public Integer getVal() {
        return val;
    }

    public void setVal(Integer val) {
        this.val = val;
    }

    public BinaryNode getLeft() {
        return left;
    }

    public void setLeft(BinaryNode left) {
        this.left = left;
    }

    public BinaryNode getRight() {
        return right;
    }

    public void setRight(BinaryNode right) {
        this.right = right;
    }
}
