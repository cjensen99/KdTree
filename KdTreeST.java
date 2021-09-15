package a05;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;

public class KdTreeST<Value> {
	private Node root;
	private double xmin;
	private double xmax;
	private double ymin;
	private double ymax;
	private int size;
	
	private class Node {
        private Point2D p;           // sorted by key
        private Value val;         // associated data
        private Node left, right;  // left and right subtrees
        private RectHV rect;	   // rectangle of the node
        private boolean vertical;  // is it verticle

        public Node(Point2D p, Value val, RectHV rect, Node left, Node right, boolean vertical) {
            this.p = p;
            this.val = val;
            this.rect = rect;
            this.left = left;
            this.right = right;
            this.vertical = vertical;
        }
    }
	
	// construct an empty symbol table of points
	public KdTreeST() {
		root = null;
		size = 0;
	}
	
	// is the symbol table empty?
	public boolean isEmpty() {
		return size == 0;
	}
	
	// number of points
	public int size() {
		return size;
	}
	
	// associate the value val with point p
	public void put(Point2D p, Value val) {
		if (p == null || val == null) throw new NullPointerException();
		xmin = Double.NEGATIVE_INFINITY;
		ymin = Double.NEGATIVE_INFINITY;
		xmax = Double.POSITIVE_INFINITY;
		ymax = Double.POSITIVE_INFINITY;
		root = put(root, p, val, true);
	}
	
	private Node put(Node node, Point2D p, Value val, boolean vertical) {		
		if (node == null) {
			size++;
			RectHV rect = new RectHV(xmin, ymin, xmax, ymax);
			return new Node(p, val, rect, null, null, true);
		}
		
		if (p.equals(node.p)) {
			node.val = val;
			return node;
		}
		
		double cmp;
		
		// if vertical compare x's
		// if vertical == false, compare y's
		if (node.vertical) cmp = Double.compare(p.x(), node.p.x());
		else cmp = Double.compare(p.y(), node.p.y());
		
		if (cmp < 0) {
			if(node.vertical) xmax = node.p.x();
			else ymax = node.p.y();
			node.left = put(node.left, p, val, !node.vertical);
		}
		
		else if (cmp >= 0) {
			if(node.vertical) xmin = node.p.x();
			else ymin = node.p.y();
			node.right = put(node.right, p, val, !node.vertical);
		}		
		return node;
	}		
	
	// value associated with point p
	public Value get(Point2D p) {
		if (p == null) throw new NullPointerException();
		return get(root, p); //TODO
	}
	
	private Value get(Node x, Point2D p) {
		if(x == null) return null;
		if(p.equals(x.p)) return x.val;
		
		double cmp;
		
		if (x.vertical) cmp = Double.compare(p.x(), x.p.x());
		else cmp = Double.compare(p.y(), x.p.y());
		
		if(cmp < 0) return get(x.left, p);
		if(cmp >= 0) return get(x.right, p);
		
		return x.val;
	}
	
	// does the symbol table contain point p?
	public boolean contains(Point2D p) {
		if (p == null) throw new NullPointerException();
		return get(p) != null;
	}
	
	// all points in the symbol table in level order
	public Iterable<Point2D> points() {
		Queue<Node> queue1 = new Queue<Node>();
		Queue<Point2D> queue2 = new Queue<Point2D>();
		
		queue1.enqueue(root);
		
		while (!queue1.isEmpty()) {
			Node x = queue1.dequeue();
			if(x == null) continue;
			queue2.enqueue(x.p);
			queue1.enqueue(x.left);
			queue1.enqueue(x.right);
		}
		
		return queue2;
	}
	
	// all points that are inside the rectangle
	public Iterable<Point2D> range (RectHV rect) {
		if (rect == null) throw new NullPointerException();
		Queue<Point2D> queue = new Queue<Point2D>();
		range(root, rect, queue);
		return queue;
	}
	
	private void range(Node x, RectHV rect, Queue<Point2D> queue) {
		if (x == null) return;
		
		if(rect.intersects(x.rect)) {
			if(rect.contains(x.p)) queue.enqueue(x.p);
			range(x.left, rect, queue);
			range(x.right, rect, queue);
		}
	}
	
	// a nearest neighbor to point p; null if the symbol table is empty
	public Point2D nearest(Point2D p) {
		if (p == null) throw new NullPointerException();
		return nearest(root, p, root.p, true);
	}
	
	private Point2D nearest(Node node, Point2D queryPoint, Point2D p, boolean vertical) {		
		Point2D nearest = p;
		
		if (node == null) return nearest;
		
		if(node.p.distanceSquaredTo(queryPoint) < nearest.distanceSquaredTo(queryPoint)) nearest = node.p;
		
		if(node.rect.distanceSquaredTo(queryPoint) < nearest.distanceSquaredTo(queryPoint)) {
			double cmp;
			if(node.vertical) cmp = Double.compare(node.p.x(), queryPoint.x());
			else cmp = Double.compare(node.p.y(), queryPoint.y());
			
			if(cmp < 0) {
				nearest = nearest(node.left, queryPoint, nearest, !node.vertical);
				nearest = nearest(node.right, queryPoint, nearest, !node.vertical);
			}
			
			if(cmp >= 0) {
				nearest = nearest(node.right, queryPoint, nearest, !node.vertical);
				nearest = nearest(node.left, queryPoint, nearest, !node.vertical);
			}				
		}
		return nearest;
	}

	public static void main(String[] args) {
		RectHV rect = new RectHV(0, 0, 3, 3);
//		System.out.println(rect);
		
		KdTreeST kd = new KdTreeST();
		
//		kd.put(new Point2D(4,4), 4);
//		kd.put(new Point2D(5,5), 5);
//		kd.put(new Point2D(2,2), 2);
		kd.put(new Point2D(1,1), 1);
		kd.put(new Point2D(2,2), 2);
//		kd.put(new Point2D(4,4), 4);
//		kd.put(new Point2D(3,3), 3);
		kd.put(new Point2D(5,5), 5);
		
		
		
//		System.out.println(kd.range(rect));
//		System.out.println(kd.contains(new Point2D(4,4)));
//		System.out.println(kd.points());
//		System.out.println(kd.get(new Point2D(3,3)));
		System.out.println(kd.size());
		
	}

}
