package common.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author soso
 * @date 2018年6月1日 上午8:51:23
 */
// 通过先序方式将数组依次添加到二叉树
public class BinaryTree<E> {
    public static class Node<E> {
        Node<E> left = null; // 左子树
        Node<E> right = null; // 右子树
        E data = null; // 数据域

        // 初始化节点
        public Node(E data) {
            this.data = data;
            this.left = null;
            this.right = null;
        }

        public Node() {

        }
    }

    private Node<E> root = null; // 根节点
    private List<Node<E>> list = null; // 节点列表，用于将数组元素转化为节点

    public Node<E> getRoot() {
        return this.root;
    }

    // 将将数组转化为一颗二叉树，转换规则：依次为节点列表中节点的左右孩子赋值。左孩子为i*2+1,右孩子为i*2+2
    @SuppressWarnings("unchecked" )
    public void createTreeByArray(Object[] array) {
        this.list = new ArrayList<Node<E>>();

        // 将数组添加到节点列表
        for (int i = 0; i < array.length; i++) {
            list.add(new Node<E>((E) array[i]));
        }

        System.out.println("头节点->" + list.get(0).data);
        this.root = new Node<E>(list.get(0).data); // 根节点

        // 为二叉树指针赋值
        for (int j = 0; j < (list.size() / 2); j++) {
            try {
                // 为左子树赋值 j*2+1
                list.get(j).left = list.get(j * 2 + 1);
                System.out.println("节点" + list.get(j).data + "左子树 ->" + list.get(j * 2 + 1).data);
                // 为右子树赋值 j*2+2
                list.get(j).right = list.get(j * 2 + 2);
                System.out.println("节点" + list.get(j).data + "右子树 ->" + list.get(j * 2 + 2).data);
            } catch (Exception e) {

            }
        }

    }

    // 先序遍历二叉树
    public void Indorder(Node<E> root) {
        if (root == null) {
            return;
        }
        System.out.println(root.data);
        Indorder(root.left); // 递归输出左子树
        Indorder(root.right); // 递归遍历右子树
    }

    // 中序遍历二叉树
    public void inOrderTraverse(Node<E> root) {
        if (root == null) {
            return;
        }
        inOrderTraverse(root.left); // 遍历左子树
        System.out.println(root.data);
        inOrderTraverse(root.right); // 遍历右子树
    }

    // 后序遍历
    public void postOrderTraverse(Node<E> root) {
        if (root == null) {
            return;
        }
        postOrderTraverse(root.left); // 遍历左子数节点
        postOrderTraverse(root.right); // 遍历右子树节点
        System.out.println(root.data); // 从下往上遍历
    }

    public static void main(String[] args) {
        BinaryTree<Integer> createTreeByArray = new BinaryTree<Integer>();
        Object[] arrays = {new Integer(1), new Integer(2), new Integer(3), new Integer(4), 5, 6, 7, 8, 9, 10};
        createTreeByArray.createTreeByArray(arrays);
        System.out.println("===============================" );
        createTreeByArray.postOrderTraverse(createTreeByArray.list.get(0));

    }

}