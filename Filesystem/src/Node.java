import java.util.ArrayList;

/**
 * Created by Rickard on 2014-12-16.
 */
public class Node
{
    Node parent;
    ArrayList children = new ArrayList<Node>();
    Entry data;

    Node(Node parent, Entry data)
    {
        // If parent parameter is null it means that this is the root node, set parent to itself
        if(parent == null)
        {
            parent = this;
        }

        this.parent = parent;
        this.data = data;
    }

    public void addChild(Node node)
    {
        children.add(node);
    }

    public Node getParent()
    {
        return this.parent;
    }

    public ArrayList<Node> getChildren()
    {
        return this.children;
    }

    public Entry getData()
    {
        return this.data;
    }

    public Node getNode(String path)
    {
        // Split the path to string array
        String[] pathArray = path.split("/");

        for(int i = 0; i < pathArray.length; i++)
        {
            System.out.println("Path element: " + pathArray[i]);
        }

        // Current cursor position in the array of paths
        int pathArrayPos = 0;

        // Current cursor node
        Node curNode = this;

        boolean pathFound = true;

        // Continue traversing tree if previous path element was found and if there are more path elements to traverse
        while(pathFound && (pathArrayPos < pathArray.length))
        {
            System.out.println("Entered while loop");
            // Assume path element is not found
            pathFound = false;

            System.out.println("Path element in pos " + pathArrayPos + " is " + pathArray[pathArrayPos]);

            // Special cases for "dot" and "dot dot" directories
            if(pathArray[pathArrayPos].equals("."))
            {
                pathFound = true;
                System.out.println(".");
            } else if(pathArray[pathArrayPos].equals(".."))
            {
                curNode = curNode.getParent();
                pathFound = true;
                System.out.println("Go one directory up (..). Parent is: " + curNode.getData().getName());
            } else
            {
                // Get children of the cursor node
                ArrayList<Node> children = curNode.getChildren();

                for (Node child : children)
                {
                    System.out.println("Child at path array pos" + pathArrayPos + ": " + child.getData().getName());
                }

                // Search the children of cursor node for cursor path element
                for (int i = 0; i < children.size() && !pathFound; i++)
                {
                    // Get child
                    Node child = children.get(i);

                    // Check if the Entry.name of the cursor node equals the path element
                    if (child.getData().getName().equals(pathArray[pathArrayPos]))
                    {
                        // Path is found and cursor node is set to the child node
                        System.out.println("Child was found.");
                        pathFound = true;
                        curNode = child;
                    }
                }
            }

            // Move one position forward in the path array
            pathArrayPos++;

            System.out.println("curNode data: " + curNode.getData().getName());
            System.out.println("Path array pos: " + pathArrayPos);
        }

        // Make sure the entire path array has been processed and that the last path element was found
        if(pathArrayPos == pathArray.length && pathFound)
        {
            return curNode;
        } else
        {
            return null;
        }
    }
}
