package com.example.datastructure_database;

import android.util.Log;
import android.widget.ListView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Database <T extends Comparable<T>> implements Serializable {
    public static class Node <T extends Comparable <T>> implements Serializable{
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


        public String get(String index){
            String ValueFound=null;
            Node<T> temp=head;

            if(head==null){
                Log.d("get()","Head is returning null");
                return ValueFound;
            }else{
                while(temp!=null){
                    String CurrentIndex= temp.index;

                    if(CurrentIndex.equals(index.trim())){
                        ValueFound=temp.element.toString();
                        Log.d("getElement()", "Element found in Index"+temp.index);
                        return ValueFound;
                    }
                    temp=temp.next;
                }

            }
            return ValueFound;
        }

        public boolean delete(String index ){
            Node<T> temp=head;

            if(head==null){
                Log.d("Delete()","Head is returning null");
                return false;
            }else{
                while(temp!=null){
                    String CurrentIndex= temp.index;

                    if(CurrentIndex.equals(index.trim())){
                        Log.d("delete()", "Element found in Index"+temp.index);
                        temp.prev.next=temp.next;
                        temp.next.prev= temp.prev;
                        temp=null;
                        Log.d("delete()", "Successfully deleted"+temp.index+" with value "+temp.element);

                        return true;
                    }
                    temp=temp.next;
                }

            }
        return false;
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



        public List <Map<String,Object>> getHashMapValues(boolean sort){

           List <Map <String,Object>>listMap = new ArrayList<>();


                if(head==null){
                    //EMPTY DATABASE
                    return null;
                }else{
                    Node<T> temp = head;

                    while(temp!=null){
                        Map <String,Object> map = new HashMap<>();
                        map.put("Index", temp.index.toString());
                        map.put("ValueType", temp.type.toString());
                        map.put("Value", temp.element.toString());
                        temp=temp.next;

                        listMap.add(map);
                    }
                }
                Log.d("listview","Values:" + listMap);

               if(sort){
                   return Sorting(listMap);
               }






            return listMap;
        }


        public boolean clear(){


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

            return true;
        }


        public List<Map<String,Object>> Sorting(List<Map<String,Object>> listMap){
            Comparator<Map<String, Object>> comparator = Comparator.comparing(m -> m.get("Index").toString());

            // Sort the listMap using the custom Comparator
            Collections.sort(listMap, comparator);

            return listMap;
        }


    }


