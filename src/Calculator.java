import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

class Calculator {

    // 후위전환식용 스택, 큐, 리스트, 임시문자
    private Stack<Character> stack = new Stack<>();
    private Queue<Character> q = new LinkedList<>();
    private List<String> postFix = new ArrayList<>(){};
    private char temp = ' ';
    // 연산용 스택, 리스트
    private Stack<Integer> computeStack = new Stack<>();
    private List<Integer> computeList = new ArrayList<>();

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

    void postFix (String inputString) throws IllegalArgumentException{
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
        return;
    }

    void computeNumber(){

        for (String s : postFix) {
            char c= s.charAt(0);
            Integer number=0;
            if (c>= '0' && c<= '9'){
                number= Integer.valueOf(s);
                System.out.println("이번 숫자는 "+ number);
                computeStack.push(number);
            }

            if (c== '+'|| c== '-'|| c== '*'|| c== '/'|| c== '('|| c== '%'){
                int right= computeStack.pop();
                System.out.println("오른숫자는: "+ right);
                int left= computeStack.pop();
                System.out.println("왼 숫자는: "+ left);
                //향상된 스위치문
                //오 좋은데.
                int result = switch (c) {
                    case '+' -> left + right;
                    case '-' -> left - right;
                    case '*', '(' -> left * right;
                    case '/' -> left / right;
                    case '%' -> left % right;
                    default -> 0;
                };
                System.out.println("연산결과 "+ result);
                computeStack.push(result);
            }
        }
        Integer computeResult= computeStack.pop();
        System.out.println("연산결과는: " + computeResult);
        computeList.add(computeResult);
        return;
    }

    void work(String inputFormula){
        stack.clear();
        q.clear();
        postFix.clear();
        computeStack.clear();
        
        postFix(inputFormula);
        computeNumber();
    }
    boolean isListEmpty(){return computeList.isEmpty();}
    int getSize(){return computeList.size();}
    void checkResult(int i){
        System.out.println(i+ "번 결과는: "+ computeList.get(i-1));
    }
    void deleteResult(int i){
        computeList.remove(i-1);
    }
    void clearComputeList(){ computeList.clear();}
}
