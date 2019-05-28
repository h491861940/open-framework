import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * @Auther: hsj
 * @Date: 2019/1/25 14:45
 * @Description:
 */
public class Lambda {
    public static void main(String[] args) {
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("我是线程");
            }
        });
        thread.start();
        Thread thread1=new Thread(()-> {System.out.println("我是新的线程");System.out.println("我是新的线程");});
        thread1.start();
        List<String> list= Arrays.asList(new String[]{"b","c","a"});
        Collections.sort(list, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });
        System.out.println(list);
        List<String> list1= Arrays.asList(new String[]{"b","a","d"});
        Collections.sort(list1,(o1, o2) -> o1.compareTo(o2));
        Collections.sort(list1,String::compareTo);
        System.out.println(list1);
        List<String> proNames = Arrays.asList(new String[]{"Ni","Hao","Lambda"});
        List<String> lowercaseNames1 = proNames.stream().map(name -> name.toLowerCase()).collect(Collectors.toList());
        List<String> lowercaseNames3 = proNames.stream().map(String::toLowerCase).collect(Collectors.toList());
        Test test=new Test("a","1");
        Test test2=new Test("b","2");
        List<Test> listTest=new ArrayList();
        listTest.add(test);
        listTest.add(test2);
        listTest.forEach(t-> System.out.println(t.getCode()));
        List<String> codeList=listTest.stream().map(t-> t.getCode()).collect(Collectors.toList());
        //testa->testa,第一个是拿到要循环的对象,->后面的是要返回的结果,因为这里返回对象,所以是对象,如果返回名称,可以 ->testa.getName()
        //最后一个,是在key重复时候,如何处理
        Map<String ,Object> mapTest=listTest.stream().collect(Collectors.toMap(Test::getName, testa -> testa,(k1, k2)->k1));
        System.out.println("mapTest"+mapTest.toString());
        System.out.println(codeList);
        System.out.println(listTest.stream().filter(test1 -> test1.getCode().equals("1")).collect(Collectors.toList()));
        List<Integer> nums = Arrays.asList(new Integer[]{1,1,null,2,3,4,null,5,6,7,8,9,10});
        System.out.println("sum is:"+nums.stream().filter(num -> num != null).distinct().mapToInt(num -> num * 2).peek(System.out::println).skip(2).limit(4).sum());

        //这段代码演示了上面介绍的所有转换方法（除了flatMap），简单解释一下这段代码的含义：
        // 给定一个Integer类型的List，获取其对应的Stream对象，然后进行过滤掉null，再去重，
        // 再每个元素乘以2，再每个元素被消费的时候打印自身，在跳过前两个元素，
        // 最后去前四个元素进行加和运算。

        Test test3=new Test("a","1");
        Test test4=new Test("b","2");
        Test test5=new Test("b","2");
        List<Test> listTest1=new ArrayList();
        listTest1.add(test3);
        listTest1.add(test4);
        listTest1.add(test5);
        Map<String, List<Test>> groupBy = listTest1.stream().collect(Collectors.groupingBy(Test::getCode));
        System.out.println(groupBy.toString());
    }
}
class Test{
    private String name;
    private String code;

    public Test(String name, String code) {
        this.name = name;
        this.code = code;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
