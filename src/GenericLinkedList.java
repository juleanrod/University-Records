import java.util.*;
public class GenericLinkedList <T extends Comparable<T>>{


    //represent the head an tail of the singly linked list
    private Node<T> first = null;
    private Node<T> last = null;
    public static int count = 0;

     public class Node<T>{
        T value;
        Node<T> next = null;

        public Node(T value){
            this.value = value;
            this.next = null;
        }
    }


    public void add (T element){
        Node<T> newnode = new Node<T>(element);

        if(first==null){
            first = newnode;
            last = newnode;
        }
        else{
            Node<T> lastnode = gotolastnode(first);
            lastnode.next = newnode;
        }
        count++;
    }


    public T get(int pos)
    {
        Node<T> Nodeptr = first;
        int hopcount=0;
        while (hopcount < count && hopcount<pos)
        {   if(Nodeptr!=null)
        {
            Nodeptr = Nodeptr.next;
        }
            hopcount++;
        }
        return  Nodeptr.value;
    }

    private Node<T> gotolastnode(Node<T> nodepointer) {
        if (nodepointer == null) {
            return nodepointer;
        } else {
            if (nodepointer.next == null)
                return nodepointer;
            else
                return gotolastnode(nodepointer.next);

        }


    }

    public void sortList(){
         Node<T> currentNode = first;
         Node<T> index = null;
         T temp;

         if(first == null){
             return;
         }else{
             while (currentNode != null){
                 //node index will point to node next to current
                 index = currentNode.next;
                 while(index != null) {
                     //If current node's value is greater than index's value, swap the values
                     if(currentNode.value.compareTo(index.value) > 0){
                        temp = currentNode.value;
                        currentNode.value = index.value;
                        index.value = temp;
                     }
                     index = index.next;
                 }currentNode = currentNode.next;
             }
         }
    }
    public void display(){
         Node<T> current = first;
         if(first == null){
             System.out.println("List is empty");
             return;
         }
         while(current != null){
             System.out.print(current.value + " ");
             current = current.next;
         }
        System.out.println();
    }





}
