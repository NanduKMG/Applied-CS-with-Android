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

package com.google.engedu.bstguesser;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;

public class TreeNode {
    private static final int SIZE = 60;
    private static final int MARGIN = 20;
    private int value, height;
    protected TreeNode left, right;
    private boolean showValue;
    private int x, y;
    private int color = Color.rgb(150, 150, 250);
    public TreeNode parent;

    public TreeNode(int value) {
        this.value = value;
        this.height = 0;
        showValue = false;
        left = null;
        right = null;
        parent = null;
    }

    public TreeNode getRoot(TreeNode t){
        while (true){
            if(t.parent==null)
                return t;
            else
                t=t.parent;
        }
    }

    public TreeNode insert(int valueToInsert) {
        TreeNode temp = this ;
        TreeNode leaf;
        while(true){
            if(valueToInsert<temp.value){
                if(temp.left == null){
                    temp.left = new TreeNode(valueToInsert);
                    leaf = temp.left;
                    temp.left.parent = temp;
                    break;
                }
                else{
                    temp = temp.left;
                }
            }
            else {
                if(temp.right == null){
                    temp.right = new TreeNode(valueToInsert);
                    leaf = temp.right;
                    temp.right.parent = temp;
                    break;
                }
                else{
                    temp = temp.right;
                }

            }
        }
        setHeight(this);
        Log.d("leaf", String.valueOf(leaf.getValue()));
        avlBalance(leaf);

        return getRoot(leaf);

    }

    private void fullDetails(TreeNode t){
        Log.d("Value:",String.valueOf(t.getValue()));
        if(t.parent!=null)
            Log.d("Parent",String.valueOf(t.parent.getValue()));
        else
            Log.d("Parent","NULL");

        if(t.left!=null){
            Log.d("left",String.valueOf(t.left.getValue()));
        }
        else{
            Log.d("left","NULL");
        }
        if(t.right!=null){
            Log.d("right",String.valueOf(t.right.getValue()));
        }
        else{
            Log.d("right","NULL");
        }

    }

    private void avlBalance(TreeNode w){
        //find first unbalanced node Z
        TreeNode x,y,z;
        z = w.parent;

        while (true){
            Log.d("parent",String.valueOf((z.getValue())));
            Log.d("balance factor", String.valueOf(z.getBalanceFactor()));
            if( z.getBalanceFactor() > 1) {
            //    Log.d("unbalanced", String.valueOf(z.getValue()));
                break;
            }
            if(z == null)
                break;


            if(z.parent!=null){
                z=z.parent;
            }
            else{
                z = null;
                break;
            }
        }

        if(z == null)
            return;

//        if(z.height<3)
//            return;


        Log.d("first unbalanced", String.valueOf(z.getValue()));
        Log.d("first unbalanced bf", String.valueOf(z.getBalanceFactor()));


        Log.d("Z value","");
        fullDetails(z);

        //find Y - child and X - grand child
        if(w.getValue()<z.getValue()){
            y = z.left;
            Log.d("y", String.valueOf(y.getValue()));
            fullDetails(y);
            if(w.getValue()<y.getValue()){
                x = y.left;
                Log.d("x", String.valueOf(x.getValue()));
                fullDetails(x);
                rightRotate(z);
            }
            else {
                x = y.right;
                Log.d("x", String.valueOf(x.getValue()));
                fullDetails(x);
                leftRotate(y);
                rightRotate(z);
            }
        }
        else {
            y = z.right;
            Log.d("y", String.valueOf(y.getValue()));
            fullDetails(y);
            if(w.getValue()<y.getValue()){
                x = y.left;
                Log.d("x", String.valueOf(x.getValue()));
                fullDetails(x);
                rightRotate(y);
                leftRotate(z);
            }
            else {
                x = y.right;
                Log.d("x", String.valueOf(x.getValue()));
                fullDetails(x);
                leftRotate(z);
            }
        }
        Log.d("Z,Y and X","");
        fullDetails(z);
        fullDetails(y);
        fullDetails(x);



    }

    private  void rightRotate(TreeNode z){
        TreeNode leftChild;
        leftChild = z.left;
        leftChild.parent = z.parent;
        if(z.parent!=null) {
            if (leftChild.value < z.parent.getValue()) {
                z.parent.left = leftChild;
            } else
                z.parent.right = leftChild;
        }

        z.parent = leftChild;
        z.left = leftChild.right;
        leftChild.right = z;
        Log.d("right","rotated right man");
        fullDetails(z);
    }

    private  void leftRotate(TreeNode z){
        TreeNode rightChild;
        rightChild = z.right;
        rightChild.parent = z.parent;
        if(z.parent!=null) {
            if (rightChild.value < z.parent.getValue()) {
                z.parent.left = rightChild;
            } else
                z.parent.right = rightChild;
        }
        z.parent = rightChild;
        z.right = rightChild.left;
        rightChild.left = z;

        Log.d("Left","rotated left man");
        fullDetails(z);
    }



    private void setHeight(TreeNode root){
        if( root == null){
            return;
        }

        setHeight(root.left);
        setHeight(root.right);

        if(root.left!=null){
            if(root.right!=null){
                root.height = Math.max(root.left.height,root.right.height) + 1;
            }
            else{
                root.height = root.left.height+1;
            }
        }
        else {
            if(root.right!=null){
                root.height = root.right.height+1;
            }
            else{
                root.height = 0;
            }
        }

    }

    public int getBalanceFactor(){
        int left,right;
        left = (this.left == null)?-1:this.left.height;
        right = (this.right == null)?-1:this.right.height;
        return Math.abs(left-right);
    }

    public int getValue() {
        return value;
    }

    public void positionSelf(int x0, int x1, int y) {
        this.y = y;
        x = (x0 + x1) / 2;

        if(left != null) {
            left.positionSelf(x0, right == null ? x1 - 2 * MARGIN : x, y + SIZE + MARGIN);
        }
        if (right != null) {
            right.positionSelf(left == null ? x0 + 2 * MARGIN : x, x1, y + SIZE + MARGIN);
        }
    }

    public void draw(Canvas c) {
        Paint linePaint = new Paint();
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(3);
        linePaint.setColor(Color.GRAY);
        if (left != null)
            c.drawLine(x, y + SIZE/2, left.x, left.y + SIZE/2, linePaint);
        if (right != null)
            c.drawLine(x, y + SIZE/2, right.x, right.y + SIZE/2, linePaint);

        Paint fillPaint = new Paint();
        fillPaint.setStyle(Paint.Style.FILL);
        fillPaint.setColor(color);
        c.drawRect(x-SIZE/2, y, x+SIZE/2, y+SIZE, fillPaint);

        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(SIZE * 2/3);
        paint.setTextAlign(Paint.Align.CENTER);
        c.drawText(showValue ? String.valueOf(value) : "?", x, y + SIZE * 3/4, paint);

        if (height > 0) {
            Paint heightPaint = new Paint();
            heightPaint.setColor(Color.MAGENTA);
            heightPaint.setTextSize(SIZE * 2 / 3);
            heightPaint.setTextAlign(Paint.Align.LEFT);
            c.drawText(String.valueOf(height), x + SIZE / 2 + 10, y + SIZE * 3 / 4, heightPaint);
        }

        if (left != null)
            left.draw(c);
        if (right != null)
            right.draw(c);
    }

    public int click(float clickX, float clickY, int target) {
        int hit = -1;
        if (Math.abs(x - clickX) <= (SIZE / 2) && y <= clickY && clickY <= y + SIZE) {
            if (!showValue) {
                if (target != value) {
                    color = Color.RED;
                } else {
                    color = Color.GREEN;
                }
            }
            showValue = true;
            hit = value;
        }
        if (left != null && hit == -1)
            hit = left.click(clickX, clickY, target);
        if (right != null && hit == -1)
            hit = right.click(clickX, clickY, target);
        return hit;
    }

    public void invalidate() {
        color = Color.CYAN;
        showValue = true;
    }
}
