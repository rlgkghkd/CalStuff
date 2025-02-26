import java.util.*;

//TIP 코드를 <b>실행</b>하려면 <shortcut actionId="Run"/>을(를) 누르거나
// 에디터 여백에 있는 <icon src="AllIcons.Actions.Execute"/> 아이콘을 클릭하세요.
public class Main {
    public static void main(String[] args) {
        Scanner sc= new Scanner(System.in);
        while (true) {
            // 입력 받음
            System.out.println("수식 입력");
            String inputFormula= sc.nextLine();
            System.out.println("입력받은 수식: "+ inputFormula);
            // 입력 exit면 탈출
            if (inputFormula.equals("exit")) {break;}

            // 수식정리: 빈칸 없앰
            inputFormula= inputFormula.replaceAll(" ", "");

            // 스택에 기호 저장, 큐에 숫자 저장, postFix에 수식 구현
            // 굳이 큐를 쓰는 이유는 괄호 이후에 곱셈기호를 생략하고 그냥 숫자만 적을 수 있으므로
            // 괄호 앞에 숫자가 있는지를 체크하기 위해 괄호가 스택으로 갈 때 큐에 숫자가 없으면 큐에 1을 넣어준다.
            Stack<Character> stack = new Stack<>();
            Queue<Character> q= new LinkedList<>();
            List<Character> postFix = new ArrayList<>();
            char temp= ' ';

            // 후위 전환식으로 구현
            // think of 12+3(4-5)/6+7(8*9)
            // will become  12 3 4 5-( 6 /+ 7 8 9*(
            for( Character c : inputFormula.toCharArray()){
                if (c>= '0' && c<= '9'){
                    // 현재 문자가 숫자인 경우
                    // 숫자는 큐에 들어간다.
                    q.add(c);

                } else if (c== '*'|| c== '/'|| c== '%') {
                    // 곱셈, 나눗셈, 모듈러 연산인 경우
                    // 큐에 있는 모든 숫자를 수식에 넣고
                    // 기호는 스택에 넣는다.
                    while (!(q.peek()== null)){
                        postFix.add(q.poll());
                    }

                    //괄호 이전이 숫자였을 경우 빈칸을 더해 숫자임을 구분하게 한다.
                    if (temp>= '0' && temp<= '9'){
                        postFix.add(' ');
                    }

                    stack.push(c);

                } else if (c== '(' ) {
                    // 앞 괄호인 경우
                    // 현재 큐에 숫자가 있는지 체크하고
                    // 없으면 1을 더해준다(괄호를 곱셈기호로 사용할 예정
                    if (q.peek()== null) {
                        q.add('1');
                        q.add(' ');
                    }

                    while (!(q.peek()== null)){
                        postFix.add(q.poll());
                    }

                    if (temp>= '0' && temp<= '9'){
                        postFix.add(' ');
                    }

                    stack.push(c);

                } else if (c== ')') {
                    // 뒤 괄호인 경우
                    // 현재 큐 숫자를 모두 수식에 추가한다.
                    // 스택에서 괄호까지 전부 수식에 더해준다
                    while (!(q.peek()== null)){
                        postFix.add(q.poll());
                    }
                    
                    if (temp>= '0' && temp<= '9'){
                        postFix.add(' ');
                    }

                    while (!stack.isEmpty() && stack.peek() != '(') {
                        postFix.add(stack.pop());
                    }
                    if (!stack.isEmpty()){
                        postFix.add(stack.pop());
                    }

                } else if (c== '+'|| c== '-') {
                    // 덧셈, 뺄셈기호의 경우
                    // 큐 숫자를 전부 수식에 추가한다.
                    // 이후 괄호가 나오기 전까지 스택에 있는 모든 기호를 수식에 더한다.
                    // 덧셈, 뺄셈은 맨 마지막에 연산되므로.
                    while (!(q.peek()== null)){
                        postFix.add(q.poll());
                    }
                    if (temp>= '0' && temp<= '9'){
                        postFix.add(' ');
                    }
                    while (!stack.isEmpty() && stack.peek() != '(') {
                        postFix.add(stack.pop());
                    }
                    stack.push(c);
                }
                temp= c;
            }

            // 못 턴 기호나 숫자를 마지막으로 털어준다.
            // 이유나 원인까지는 알겠는데, 이걸로 발생할 오류는 예측 못하겠다.
            while (!(q.peek()== null)){
                postFix.add(q.poll());
            }
            if (temp>= '0' && temp<= '9'){
                postFix.add(' ');
            }
            while (!stack.isEmpty()) {
                postFix.add(stack.pop());
            }

            String postfixString= postFix.toString();
            postfixString= postfixString.replaceAll("[\\[\\]]", "");
            String[] splited= postfixString.split(", ");

            //12 3 4 5 -(6 /+7 8 9 *( 이런 문자열이 만들어진다.

            //이제 숫자를 스택에 넣고, 기호가 나올 때 마다 스택에서 숫자를 꺼내 연산한다.
            //연산 결과를 다시 스택에 넣는다.
            //연속된 숫자는 하나로 넣고, 공백이 있는 숫자는 별개로 넣는다.
            //현재 나눗셈 밒 모듈러 등 소수점 관련 연산은 결과가 정상적으로 나오지 않는다.
            //소수부분 더해줄 때 그냥 Double로 바꿔라.
            Stack<Integer> computeStack = new Stack<>();
            String numberTemp= "";
            for (String s : splited) {
                char c= s.charAt(0);
                Integer number=0;
                if (c>= '0' && c<= '9'){
                    number= Integer.parseInt(s);
                }
                if (number>=0 && number<=9 && c!= ' '){
                    numberTemp= numberTemp+ number.toString();
                }

                if (c== ' '){
                    computeStack.push(Integer.parseInt(numberTemp));
                    numberTemp= "";
                }

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
            System.out.println("연산결과는: " + computeStack.pop());
        }
        sc.close();
    }
}
//죽여줘.