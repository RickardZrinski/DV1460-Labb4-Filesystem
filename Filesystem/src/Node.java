import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Rickard on 2014-12-16.
 */
public class Node implements Serializable
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

        // Current cursor position in the array of paths
        int pathArrayPos = 0;

        // Current cursor node
        Node curNode = this;

        boolean pathFound = true;

        // Continue traversing tree if previous path element was found and if there are more path elements to traverse
        while(pathFound && (pathArrayPos < pathArray.length))
        {
            // Assume path element is not found
            pathFound = false;

            // Special cases for "dot" and "dot dot" directories
            if(pathArray[pathArrayPos].equals("."))
            {
                pathFound = true;
            } else if(pathArray[pathArrayPos].equals(".."))
            {
                curNode = curNode.getParent();
                pathFound = true;
            } else
            {
                // Get children of the cursor node
                ArrayList<Node> children = curNode.getChildren();

                // Search the children of cursor node for cursor path element
                for (int i = 0; i < children.size() && !pathFound; i++)
                {
                    // Get child
                    Node child = children.get(i);

                    // Check if the Entry.name of the cursor node equals the path element
                    if (child.getData().getName().equals(pathArray[pathArrayPos]))
                    {
                        // Path is found and cursor node is set to the child node
                        pathFound = true;
                        curNode = child;
                    }
                }
            }

            // Move one position forward in the path array
            pathArrayPos++;
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

    public String getPath()
    {
        if(this.equals(this.getParent().getParent()))
        {
            return this.getData().getName();
        } else
        {
            return this.getParent().getPath() + this.getData().getName() + "/";
        }
    }

    public boolean remove(String path)
    {
        boolean isRemoved;

        Node node = this.getNode(path);

        // Only delete the node if it has no children
        if(node.getChildren().size() == 0)
        {
            ArrayList<Node> pChildren = node.getParent().getChildren();

            pChildren.remove(node);

            isRemoved = true;
        } else
        {
            isRemoved = false;
        }

        return isRemoved;
    }
}
