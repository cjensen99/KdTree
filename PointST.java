package a05;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.RedBlackBST;

public class PointST<Value> {
	private RedBlackBST<Point2D, Value> bst;
	
	// construct an empty symbol table of points
	public PointST() {
		bst = new RedBlackBST<>();
	}
	
	// is the symbol table empty?
	public boolean isEmpty() {
		if (bst.size() == 0) return true;
		return false;
	}
	
	// number of points
	public int size() {
		return bst.size();
	}
	
	// associate the value val with point p
	public void put(Point2D p, Value val) {
		if (p == null || val == null) throw new NullPointerException();
		bst.put(p, val);
	}
	
	// value associated with point p
	public Value get(Point2D p) {
		if (p == null) throw new NullPointerException();
		return bst.get(p);
	}
	
	// does the symbol table contain point p?
	public boolean contains(Point2D p) {
		if (p == null) throw new NullPointerException();
		return bst.contains(p);
	}
	
	// all points in the symbol table
	public Iterable<Point2D> points() {
		return bst.keys();
	}
	
	// all points that are inside the rectangle
	public Iterable<Point2D> range(RectHV rect) {
		if (rect == null) throw new NullPointerException();
		Queue<Point2D> queue = new Queue<Point2D>();
		
		for (Point2D p: bst.keys()) {
			if (rect.contains(p)) queue.enqueue(p);
		}
		return queue;
	}
	
	// a nearest neighbor to point p; null if the symbol table is empty
	public Point2D nearest(Point2D p) {
		if (p == null) throw new NullPointerException();
		if (isEmpty()) return null;
		
		Point2D place = bst.max();
		
		for (Point2D point2D : bst.keys()) {
			if (p.distanceSquaredTo(point2D) < p.distanceSquaredTo(place)) {
				place = point2D;
			}
		}
		
		return place;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
