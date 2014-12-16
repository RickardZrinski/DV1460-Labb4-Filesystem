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
        // Remove current directory (.) from path
        if(path.charAt(0) == '.' && path.charAt(1) == '/')
        {
            path = path.substring(2, path.length());

            // If there is nothing in the new path, return this node
            if(path.isEmpty())
            {
                return this;
            }
        }

        String[] pathArray = path.split("/");
        for(int i = 0; i < pathArray.length; i++)
        {
            System.out.println("Path element: " + pathArray[i]);
        }

        int pathArrayPos = 0;

        Node curNode;

        // TODO Det fungerar ej att använda "..". Dessutom kan det finnas oändligt antal "..", inte bara på första positionen. Detta måste fixas.
        if(pathArray[0] == "..")
        {
            curNode = this.getParent();
            pathArrayPos++;
        } else
        {
            curNode = this;
            System.out.println("curNode = this");
        }

        boolean childFound = true;
        while(childFound && (pathArrayPos < pathArray.length))
        {
            System.out.println("Entered while loop");
            childFound = false;

            ArrayList<Node> children = curNode.getChildren();
            for(Node child: children)
            {
                System.out.println("Child at path array pos" + pathArrayPos + ": " + child.getData().getName());
            }

            for(int i = 0; i < children.size() && !childFound; i++)
            {
                Node child = children.get(i);
                if(child.getData().getName().equals(pathArray[pathArrayPos]))
                {
                    System.out.println("Child was found.");
                    childFound = true;
                    curNode = child;
                }
            }

            pathArrayPos++;

            System.out.println("curNode data: " + curNode.getData().getName());
            System.out.println("Path array pos: " + pathArrayPos);
        }

        if(pathArrayPos == pathArray.length && childFound)
        {
            return curNode;
        } else
        {
            return null;
        }
    }
}
