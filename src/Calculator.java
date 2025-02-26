
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

public class Calculator {

    // 후위전환식용 스택, 큐, 리스트, 임시문자
    Stack<Character> stack = new Stack<>();
    Queue<Character> q = new LinkedList<>();
    List<String> postFix = new ArrayList<>();
    char temp = ' ';
    

    // 수식 빈칸 제거
    String removeSpace(String inputFormula) {
        return inputFormula.replaceAll(" ", "");
    }

    void dealingNumber(Character c) {
        q.add(c);
    }

    void addNumberToPostFix(){
        String tempNumber="";
        while (q.peek()!=null && !q.isEmpty()){
            tempNumber= tempNumber+ q.poll();
        }
        if (tempNumber!=""){postFix.add(tempNumber);}
    }

    void emptyStackUntillPrearentheses() {
        while (!stack.isEmpty() && stack.peek() != '(') {
            postFix.add(stack.pop().toString());
        }
    }

    void dealingFastSymbols(Character c) {
        addNumberToPostFix();
        stack.push(c);
    }

    void dealingSlowSymbols(Character c) {
        addNumberToPostFix();
        emptyStackUntillPrearentheses();
        stack.push(c);
    }

    void dealingPrearentheses(Character c) {
        if (q.peek() == null) {
            q.add('1');
        }
        addNumberToPostFix();
        stack.push(c);
    }

    void dealingPostParentheses() {
        addNumberToPostFix();
        emptyStackUntillPrearentheses();
        postFix.add(stack.pop().toString());
    }

    void postFixingWrapUp() {
        addNumberToPostFix();
        while (!stack.isEmpty()) {
            postFix.add(stack.pop().toString());
        }
    }
    /* 
    String[] finisingPostFixing(List<Character> inputList) {
        String postfixString = inputList.toString();
        postfixString = postfixString.replaceAll("[\\[\\]]", "");
        String[] splited = postfixString.split(", ");
        for (String s : splited) {
            System.out.println(s);
        }
        System.out.println();
        return splited;
    }
    */

    List<String> postFix(String inputString){
        String[] postFixed;
        removeSpace(inputString);
        for( Character c : inputString.toCharArray()){
            switch (c) {
                case '(' -> dealingPrearentheses(c);
                case ')' -> dealingPostParentheses();
                case '+', '-' -> dealingSlowSymbols(c);
                case '*', '/' -> dealingFastSymbols(c);
                default -> {
                    if (Character.isDigit(c)){
                        dealingNumber(c);
                    } else {
                        System.out.println("잘못된 문자열이 입력되었습니다.");
                        throw new IllegalArgumentException();
                    }
                }
            }
        }
        postFixingWrapUp();
        //postFixed= finisingPostFixing(postFix);
        String postFixedString= postFix.toString();
        System.out.println("후위표기식: "+ postFixedString);
        return postFix;
    }
}
