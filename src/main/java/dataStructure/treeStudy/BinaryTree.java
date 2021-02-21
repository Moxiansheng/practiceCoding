package dataStructure.treeStudy;

public class BinaryTree {
    private BinaryNode root = null;
    public final static Integer RECURSION = 0;
    public final static Integer NOT_RECURSION = 1;

    public BinaryNode insert(Integer flag, Integer val){
        if(root == null){
            root = new BinaryNode(val);
        }else{
            if(flag == RECURSION){
                insertRec(root, val);
            }else{
                insertNotRec(val);
            }
        }
        return root;
    }

    public void insertNotRec(Integer val){
        BinaryNode temp = root;
        while(true) {
            if (temp.getVal() > val) {
                if(temp.getLeft() == null){
                    temp.setLeft(new BinaryNode(val));
                    break;
                }else{
                    temp = temp.getLeft();
                }
            }else{
                if(temp.getRight() == null){
                    temp.setRight(new BinaryNode(val));
                    break;
                }else{
                    temp = temp.getRight();
                }
            }
        }
    }

    private void insertRec(BinaryNode r, Integer val){
        if(r.getVal() > val){
            if(r.getLeft() == null){
                r.setLeft(new BinaryNode(val));
            }else{
                insertRec(r.getLeft(), val);
            }
        }else{
            if(r.getRight() == null){
                r.setRight(new BinaryNode(val));
            }else{
                insertRec(r.getRight(), val);
            }
        }
    }

    public Integer delete(Integer val){
        BinaryNode temp = root;
        while(temp != null){
            if(temp.getVal() == val){

            }
        }
        return val;
    }

    public void showTree(){
        middleOrder(root);
    }

    private void middleOrder(BinaryNode r){
        if(r == null){
            return;
        }
        middleOrder(r.getLeft());
        System.out.println(r.getVal());
        middleOrder(r.getRight());
    }
}
