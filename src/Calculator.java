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

public class Calculator<t> {

    // 후위전환식용 스택, 큐, 리스트, 임시문자
    private Stack<Character> stack = new Stack<>();
    private Queue<Character> q = new LinkedList<>();
    private List<String> postFix = new ArrayList<>(){};
    private boolean symbolize= false;
    // 연산용 스택, 리스트
    private Stack<Double> computeStack = new Stack<>();
    private List<Double> computeList = new ArrayList<>();
    private String temp= "";

    // 수식 빈칸 제거
    String removeSpace(String inputFormula) {
        return inputFormula.replaceAll(" ", "");
    }

    // 예비큐에 숫자 추가
    // 큐에 들어간 숫자는 이후 기호를 만났을 때 한 덩어리로 묶여서 후위전환식 리스트에 추가됨
    // 음수처리
    private void dealingNumber(Character c) {
        if (symbolize){
            q.add('-');
            symbolize= false;
        }
        q.add(c);
        temp=c.toString();
    }

    // 예비큐에 있는 숫자를 후위전환식 리스트에 추가
    private void addNumberToPostFix(){
        String tempNumber="";
        while (q.peek()!=null && !q.isEmpty()){
            tempNumber= tempNumber+ q.poll();
        }
        if (tempNumber!=""){postFix.add(tempNumber);}
    }

    //후괄호 탐색시 호출
    //전괄호 전까지 스택을 비우고 후위전환식 리스트에 추가
    private void emptyStackUntillPrearentheses() {
        while (!stack.isEmpty() && stack.peek() != '(') {
            postFix.add(stack.pop().toString());
        }
    }

    //빠른 기호 처리(곱셈, 나눗셈, 모듈러 등)
    //큐에 있는 숫자 리스트에 한 덩어리로 넘김
    //스택에 기호 추가가
    private void dealingFastSymbols(Character c) {
        addNumberToPostFix();
        stack.push(c);
        temp="";
    }

    //느린 기호 처리(덧셈, 뺄셈)
    //뺄셈기호가 계산식이 아닌 음수기호로 사용될 때 처리
    private void dealingSlowSymbols(Character c) {
        if (temp.isEmpty() && c== '-'){
            symbolize= true;
            temp="";
            return;
        }
        addNumberToPostFix();
        stack.push(c);
        temp="";
    }

    //전괄호 처리
    //스택에 전괄호 추가.
    //전괄호 앞에 숫자 없을 시 1 추가
    //수식에서 전괄호는 곱셈과 같은 역활을 함
    private void dealingPrearentheses() {
        if (q.peek() == null) {
            q.add('1');
        }
        addNumberToPostFix();
        stack.push('(');
        temp="";
    }

    //후괄호 처리
    //스택에 있는 기호들을 전괄호가 나오기 전까지 모두 후위전환식 리스트에 추가
    private void dealingPostParentheses() {
        addNumberToPostFix();
        emptyStackUntillPrearentheses();
        postFix.add(stack.pop().toString());
        temp="";
    }

    //후위전환식 마무리
    //큐에 있는 숫자 리스트에 한 덩어리로 넘김
    //스택에 있는 기호들을 모두 후위전환식 리스트에 추가
    private void postFixingWrapUp() {
        addNumberToPostFix();
        while (!stack.isEmpty()) {
            postFix.add(stack.pop().toString());
        }
        temp="";
    }

    //후위전환식으로 변환
    private void postFix (String inputString) {
        String workingString= removeSpace(inputString);
        System.out.println(workingString);
        for( Character c : workingString.toCharArray()){
            switch (c) {
                case '(' -> dealingPrearentheses();
                case ')' -> dealingPostParentheses();
                case '+', '-' -> dealingSlowSymbols(c);
                case '*', '/', '%' -> dealingFastSymbols(c);
                case '.'-> dealingNumber(c);
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
    }

    //후위전환식 계산
    private void computeNumber(){

        //후위전환식 문자열을 받아와 숫자일 경우 Double타입으로 캐스팅, 기호는 enum에서 명시된 동작 수행행
        for (String s : postFix) {
            //숫자일 경우 동작
            try {
                Double number= Double.valueOf(s);
                number= Double.valueOf(s);
                computeStack.push(number);
            //기호일 경우 동작
            } catch (NumberFormatException e){
                Symbols symbols= Symbols.getSymbolName(s.charAt(0));
                double right= computeStack.pop();
                double left= computeStack.pop();
                double result= symbols.stuff(left, right);
                computeStack.push((double)result);
            }
        }
        //연산결과를 출력하고 결과리스트에 추가가
        Double computeResult= computeStack.pop();
        System.out.println("연산결과는: " + computeResult+ "입니다.");
        computeList.add(computeResult);
    }

    //호출용 메소드
    //입력받은 수식을 후위전환식으로 변환 후 계산
    public void work(String inputFormula) throws IllegalArgumentException{
        stack.clear();
        q.clear();
        postFix.clear();
        computeStack.clear();
        
        postFix(inputFormula);
        computeNumber();
    }

    //리스트 관련
    //리스트 비어있는지 확인
    public boolean isListEmpty(){return computeList.isEmpty();}
    //리스트 사이트 게터
    public int getSize(){return computeList.size();}
    //결과리스트 인덱스를 받아서 출력
    public void checkResult(int i){
        System.out.println(i+ "번 결과는: "+ computeList.get(i-1));
    }
    //결과리스트 인덱스를 받아서 삭제
    public void deleteResult(int i){
        computeList.remove(i-1);
    }
    //결과리스트 전체 삭제
    public void clearComputeList(){ computeList.clear();}
    //입력된 숫자보다 큰 숫자를 결과리스트에서 찾아서 출력
    public void isBigger(Double input){
        List<Double> biggerList= computeList.stream().filter(x-> x>input).toList();
        if(biggerList.isEmpty()){
            System.out.println("입력하신 숫자보다 큰 수는 없습니다.");
        } else {
            System.out.println("입력하신 숫자보다 큰 결과는: "+ biggerList+ "입니다");
        }
    }
}
