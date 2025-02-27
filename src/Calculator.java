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

    private void dealingNumber(Character c) {
        q.add(c);
    }

    private void addNumberToPostFix(){
        String tempNumber="";
        while (q.peek()!=null && !q.isEmpty()){
            tempNumber= tempNumber+ q.poll();
        }
        if (!tempNumber.equals("")){postFix.add(tempNumber);}
    }

    private void emptyStackUntillPrearentheses() {
        while (!stack.isEmpty() && stack.peek() != '(') {
            postFix.add(stack.pop().toString());
        }
    }

    private void dealingFastSymbols(Character c) {
        addNumberToPostFix();
        stack.push(c);
    }

    private void dealingSlowSymbols(Character c) {
        addNumberToPostFix();
        emptyStackUntillPrearentheses();
        stack.push(c);
    }

    private void dealingPrearentheses(Character c) {
        if (q.peek() == null) {
            q.add('1');
        }
        addNumberToPostFix();
        stack.push(c);
    }

    private void dealingPostParentheses() {
        addNumberToPostFix();
        emptyStackUntillPrearentheses();
        postFix.add(stack.pop().toString());
    }

    private void postFixingWrapUp() {
        addNumberToPostFix();
        while (!stack.isEmpty()) {
            postFix.add(stack.pop().toString());
        }
    }

    private void postFix (String inputString) throws IllegalArgumentException{
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
    }

    private void computeNumber(){

        for (String s : postFix) {
            char c= s.charAt(0);
            if (c>= '0' && c<= '9'){computeStack.push(Integer.valueOf(s));}

            if (c== '+'|| c== '-'|| c== '*'|| c== '/'|| c== '('|| c== '%'){
                int right= computeStack.pop();
                int left= computeStack.pop();

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
                computeStack.push(result);
            }
        }
        Integer computeResult= computeStack.pop();
        System.out.println("연산결과는: " + computeResult);
        computeList.add(computeResult);
    }

    public void work(String inputFormula){
        stack.clear();
        q.clear();
        postFix.clear();
        computeStack.clear();
        
        postFix(inputFormula);
        computeNumber();
    }
    public boolean isListEmpty(){return computeList.isEmpty();}
    public int getSize(){return computeList.size();}
    public void checkResult(int i){
        System.out.println(i+ "번 결과는: "+ computeList.get(i-1));
    }
    public void deleteResult(int i){
        computeList.remove(i-1);
    }
    public void clearComputeList(){ computeList.clear();}
}
