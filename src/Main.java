import java.util.*;

//TIP 코드를 <b>실행</b>하려면 <shortcut actionId="Run"/>을(를) 누르거나
// 에디터 여백에 있는 <icon src="AllIcons.Actions.Execute"/> 아이콘을 클릭하세요.
public class Main {
    public static void main(String[] args) {
        Scanner sc= new Scanner(System.in);
        while (true) {

            Calculator calculator = new Calculator();

            // 입력 받음
            System.out.println("수식 입력");
            String inputFormula= sc.nextLine();
            System.out.println("입력받은 수식: "+ inputFormula);
            // 입력 exit면 탈출
            if (inputFormula.equals("exit")) {break;}
            
            // 후위표기식 변환
            String[] postFixed= calculator.postFix(inputFormula);


            Stack<Integer> computeStack = new Stack<>();
            String numberTemp= "";
            for (String s : postFixed) {
                char c= s.charAt(0);
                Integer number=0;
                if (c>= '0' && c<= '9'){
                    number= Integer.parseInt(s);
                    System.out.println("이번 숫자는 "+ number);
                }
                if (number>=0 && number<=9 && c!= ' '){
                    numberTemp= numberTemp+ number.toString();
                    System.out.println("더한 숫자는 "+ numberTemp);
                }

                if (c== ' '){
                    computeStack.push(Integer.parseInt(numberTemp));
                    System.out.println("푸쉬합니다: "+ numberTemp);
                    numberTemp= "";
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
            System.out.println("연산결과는: " + computeStack.pop());
        }
        sc.close();
    }
}
//죽여줘.