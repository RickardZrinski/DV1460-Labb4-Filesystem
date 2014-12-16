/**
 * Created by Rickard on 2014-12-16.
 */
public class TestTree
{
    public static void main(String[] args)
    {
        Node rootNode = new Node(null, new Entry("/", true));

        Node one = new Node(rootNode, new Entry("one", true));
        rootNode.addChild(one);

        Node two = new Node(one, new Entry("two", true));
        one.addChild(two);

        Node three = new Node(two, new Entry("three", true));
        two.addChild(three);

        Node file1 = new Node(three, new Entry("file1", false));
        three.addChild(file1);

        /*Node rootNode = new Node(null, "/");

        Node one = new Node(rootNode, "one");
        rootNode.addChild(one);

        Node two = new Node(one, "two");
        one.addChild(two);

        Node three = new Node(two, "three");
        two.addChild(three);

        Node file1 = new Node(three, "file1");
        three.addChild(file1);*/

        Node foundNode = rootNode.getNode("one/two");

        if(foundNode != null) {
            System.out.println("The node that was found has data: " + foundNode.getData().getName());
        } else
        {
            System.out.println("Node was not found");
        }
    }
}
