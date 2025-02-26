
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

public class Calculator {

    // 후위전환식용 스택, 큐, 리스트, 임시문자
    Stack<Character> stack = new Stack<>();
    Queue<Character> q = new LinkedList<>();
    List<Character> postFix = new ArrayList<>();
    char temp = ' ';

    // 수식 빈칸 제거
    String removeSpace(String inputFormula) {
        return inputFormula.replaceAll(" ", "");
    }

    void dealingNumber(Character c) {
        q.add(c);
    }

    void addCharFromQueueToList() {
        while (!(q.peek() == null)) {
            postFix.add(q.poll());
            System.out.println("현재 문자열: " + postFix);
        }
    }

    void emptyStackUntillPrearentheses() {
        while (!stack.isEmpty() && stack.peek() != '(') {
            char poped = stack.pop();
            System.out.println("스택에서 " + poped + " pop");
            postFix.add(poped);
            System.out.println("현재 문자열: " + postFix);
        }
    }

    void checkPostCharactorWasNumber() {
        if (temp >= '0' && temp <= '9') {
            postFix.add(' ');
        }
    }

    void dealingFastSymbols(Character c) {
        addCharFromQueueToList();
        checkPostCharactorWasNumber();
        stack.push(c);
    }

    void dealingSlowSymbols(Character c) {
        addCharFromQueueToList();
        checkPostCharactorWasNumber();
        emptyStackUntillPrearentheses();
        stack.push(c);
        System.out.println(c + " 를 스택에 더함");
    }

    void dealingPrearentheses(Character c) {
        if (q.peek() == null) {
            q.add('1');
            q.add(' ');
        }
        addCharFromQueueToList();
        checkPostCharactorWasNumber();
        System.out.println(c + " 를 스택에 더함");
        stack.push(c);
        System.out.println("현재 문자열: " + postFix);
    }

    void dealingPostParentheses() {
        addCharFromQueueToList();
        checkPostCharactorWasNumber();
        emptyStackUntillPrearentheses();
        if (!stack.isEmpty()) {
            postFix.add(stack.pop());
        }
    }

    void postFixingWrapUp() {
        System.out.println("마무리");
        addCharFromQueueToList();
        checkPostCharactorWasNumber();
        while (!stack.isEmpty()) {
            char poped = stack.pop();
            System.out.println("스택에서 " + poped + " pop");
            postFix.add(poped);
            System.out.println("현재 문자열: " + postFix);
        }
    }

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

    String[] postFix(String inputString){
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
            temp= c;
        }
        postFixingWrapUp();
        postFixed= finisingPostFixing(postFix);
        
        return postFixed;
    }
}
