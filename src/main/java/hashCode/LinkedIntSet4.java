package hashCode;


import dataStructure.ListStudy.SingleLinkedList;

public class LinkedIntSet4 extends singleIntSet3{
    /**
     * 链接法，每个位置放一个列表
     */

    public void Add(int item) {

        if (_values[TH(item)] == null) {
            SingleLinkedList<Integer> ls = new SingleLinkedList<Integer>();
            ls.addAtTail(item);
            _values[TH(item)] = ls;
        } else {
            SingleLinkedList<Integer> ls = (SingleLinkedList<Integer>) _values[TH(item)];
            ls.addAtTail(item);
        }
    }

    public void Remove(int item) {
        SingleLinkedList<Integer> ls = (SingleLinkedList<Integer>) _values[TH(item)];
        for (int i = 0; i < ls.size(); i++) {
            if(ls.get(i) == item){
                ls.delIndex(i);
                break;
            }
        }
    }

    public boolean Contains(int item) {
        if (_values[TH(item)] == null) {
            return false;
        } else {
            SingleLinkedList<Integer> ls = (SingleLinkedList<Integer>) _values[TH(item)];
            return ls.contains(item);
        }
    }
}
