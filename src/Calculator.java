import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

enum Symbols{
    PLUS('+') {
        @Override
        double stuff(double left, double right) {
            return left + right;
        }
    }, MINUS('-'){
        @Override
        double stuff(double left, double right) {
            return left - right;
        }
    }, MULTIPLY('*'){
        @Override
        double stuff(double left, double right) {
            return left * right;
        }
    }, DIVIDE('/'){
        @Override
        double stuff(double left, double right) {
            return left / right;
        }
    }, OPEN('('){
        @Override
        double stuff(double left, double right) {
            return left * right;
        }
    };

    private final char symbol;
    abstract double stuff(double left, double right);
    Symbols(char symbol){
        this.symbol= symbol;
    }
    //someMagicShit
    //enum 의 이름을 밸류, 밸류를 키로 쓰는 해시맵을 만든다.
    private static final Map<Character, String> SymbolMap= Collections.unmodifiableMap(Stream.of(values()).collect(Collectors.toMap(Symbols::getSymbol, Symbols::name)));
    public static Symbols getSymbolName(char symbol){return Symbols.valueOf(SymbolMap.get(symbol));}


    public char getSymbol(){return symbol;}
}

public class Calculator {

    // 후위전환식용 스택, 큐, 리스트, 임시문자
    private Stack<Character> stack = new Stack<>();
    private Queue<Character> q = new LinkedList<>();
    private List<String> postFix = new ArrayList<>(){};
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
        if (tempNumber!=""){postFix.add(tempNumber);}
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
                case '*', '/', '%' -> dealingFastSymbols(c);
                default -> {
                    if (Character.isDigit(c)){
                        dealingNumber(c);
                    } else {
                        System.out.println("잘못된 수식이 입력되었습니다.");
                        throw new IllegalArgumentException();
                    }
                }
            }
        }
        postFixingWrapUp();
        String postFixedString= postFix.toString();
        System.out.println("후위표기식: "+ postFixedString);
    }

    private void computeNumber(){

        for (String s : postFix) {
            char c= s.charAt(0);
            Integer number=0;
            if (c>= '0' && c<= '9'){
                number= Integer.valueOf(s);
                System.out.println("이번 숫자는 "+ number);
                computeStack.push(number);
            } else {
                Symbols symbols= Symbols.getSymbolName(s.charAt(0));
                int right= computeStack.pop();
                int left= computeStack.pop();
                double result= symbols.stuff(left, right);
                computeStack.push((int)result);
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
