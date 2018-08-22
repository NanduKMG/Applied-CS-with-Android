/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.touringmusician;


import android.graphics.Point;

import java.util.Iterator;

public class CircularLinkedList implements Iterable<Point> {

    private class Node {
        Point point;
        Node prev, next;

        Node(Point p,Node prev,Node next){
            point = p;
            this.prev = prev;
            this.next = next;

        }


    }

    Node head =null;

    public void insertBeginning(Point p) {

        if(head == null){
            head = new Node(p,null,null);
            head.prev=head;
            head.next=head;
        }
        else{
            Node node = new Node(p,head.prev,head);
            (head.prev).next=node;
            head.prev=node;
            head=node;
        }
    }

    private float distanceBetween(Point from, Point to) {
        return (float) Math.sqrt(Math.pow(from.y-to.y, 2) + Math.pow(from.x-to.x, 2));
    }

    public float totalDistance() {

          float total = 0;

        if (head != null)
        {
            Node temp = head;
            while (temp.next != head){
                total = total + distanceBetween(temp.point,temp.next.point);
                temp= temp.next;
            }
            total+=distanceBetween(temp.point,temp.next.point);
        }
        return total;
    }
    public void insertNearest(Point p) {
        if ( head != null) {
            Node temp = head;
            Node nearest_temp = head;
            float small = distanceBetween(head.point,p);
            float dist = Float.MAX_VALUE;
            do {
                dist = distanceBetween(p,temp.point);
                if (dist < small){
                    small = dist;
                    nearest_temp = temp;
                }
                temp = temp.next;
            }while(temp!=head);
            temp = new Node(p,null,null);
            temp.next=nearest_temp.next;
            temp.prev=nearest_temp;
            (nearest_temp.next).prev=temp;
            nearest_temp.next=temp;

        }
        else{
            insertBeginning(p);
        }
    }

    public void insertSmallest(Point p) {
        if ( head != null && head.next !=head) {
            Node temp = head;
            Node smallest_temp = head;
            float small = distanceBetween(head.point,p) + distanceBetween(head.next.point,p) - distanceBetween(head.point,head.next.point) ;
            float dist = Float.MAX_VALUE;
            do {
                dist = distanceBetween(p,temp.point) + distanceBetween(p,temp.next.point) - distanceBetween(temp.point,temp.next.point) ;
                if (dist < small){
                    small = dist;
                    smallest_temp = temp;
                }
                temp = temp.next;
            }while(temp!=head);
            temp = new Node(p,null,null);
            temp.next=smallest_temp.next;
            temp.prev=smallest_temp;
            (smallest_temp.next).prev=temp;
            smallest_temp.next=temp;

        }
        else{
            insertBeginning(p);
        }
    }

    public void reset() {
        head = null;
    }

    private class CircularLinkedListIterator implements Iterator<Point> {

        Node current;

        public CircularLinkedListIterator() {
            current = head;
        }

        @Override
        public boolean hasNext() {
            return (current != null);
        }

        @Override
        public Point next() {
            Point toReturn = current.point;
            current = current.next;
            if (current == head) {
                current = null;
            }
            return toReturn;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public Iterator<Point> iterator() {
        return new CircularLinkedListIterator();
    }


}
