package com.example.datastructure_database;

import android.util.Log;
import android.widget.ListView;

import java.io.Serializable;

public class Database <T> implements Serializable {
    public static class Node <T> implements Serializable{
            T element;
            String index;
            String type;
            Node <T> next;
            Node <T> prev;

            public Node(String index, String type, T element) {
                this.index = index;
                this.type = type;
                this.element = element;
                this.next = null;
                this.prev = null;
            }

            public Node(String index, String type, T element, Node <T> next){
                this(index, type, element);
                this.next=next;
            }
        }

        Node<T> head;
        Node<T> tail;
        private int size;

        public Database() {
            this.head = null;
            this.tail = null;
            size = 0;

        }


        public void get(String index){
            //TODO: Rachel Lim (Get At least 1 value)
        }

        public void delete(String index ){
            //TODO: Rachel Lim (Delete 1 value)
        }

        //NO USES SO FAR
        public String findValueType(T element) {

            if (element instanceof String) {
                return "string";
            } else if (element instanceof Integer || element instanceof Double) {
                return "number";
            } else if (element instanceof Character) {
                return "character";
            }

            System.out.println("findValueType() failed to return T element type");
            return null;
        }





        public String InsertNodeValueIntoDatabase(String index, String valueType, T value) {

            if(!DoesIndexExist(index)){
                Node<T> temp = new Node<>(index, valueType, value);

                if (head == null) {
                    head = temp;
                    tail = temp;
                } else {
                    temp.prev=tail;
                    tail.next = temp;
                    tail = temp;

                }
                size++;
                Log.d("Insert", "Successfully Inserted:    "+ index + valueType + value);

                return index;
            }else{
                Log.e("Insert", "Index already exists");
                return null;
            }
        }




        public boolean DoesIndexExist(String index){
            Node <T> temp = head;

            while(temp!=null){
                Log.d("DoesIndexExist()", "Index found :    "+ temp.index);

                if(temp.index.equals(index)){
                    Log.d("DoesIndexExist()", "Index exists:   "+ temp.index);
                    return true;
                }
                temp=temp.next;
            }

            return false;
        }

        public ListView Display(){

            Node <T> temp = head;

            while(temp!=null){
                Log.d("Display()", "Displaying Index:  "+ temp.index+ "    "+ temp.element+ "     "+temp.type);
                temp=temp.next;

            }
            //ListView listView = new ListView();
            //TODO: Return Display Values

            return null;
        }




        public void clear(){
            Node<T> temp= head;
            while(temp!=null){
                Node <T> next= temp.next;

                //ENSURE ALL MEMORY IS CLEARED RATHER THAN INSTANTIATING HEAD AND TAIL NULL
                temp.prev=null;
                temp.next=null;
                temp= next;
            }
            head=null;
            tail=null;
        }

    }


