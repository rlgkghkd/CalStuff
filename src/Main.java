import java.util.*;

//TIP 코드를 <b>실행</b>하려면 <shortcut actionId="Run"/>을(를) 누르거나
// 에디터 여백에 있는 <icon src="AllIcons.Actions.Execute"/> 아이콘을 클릭하세요.
public class Main {
    public static void main(String[] args) {
        Scanner sc= new Scanner(System.in);
        Calculator calculator = new Calculator();
        boolean flag= true;
        while (flag== true) {

            // 입력 받음
            System.out.println();
            System.out.println("================================================");
            System.out.println("수식을 입력하세요\n도움말이 필요할 시 hint 를 입력하세요.");
            String inputFormula= sc.nextLine();

            // 입력 exit면 탈출
            switch (inputFormula) {
                case "hint":
                    System.out.println();
                    System.out.println("================================================");
                    System.out.println("1. 수식을 입력하면 결과를 출력하고 저장합니다.");
                    System.out.println("2. 덧셈(+), 뺄셈(-), 곱셈(*), 나눗셈(/), 나머지(%)와 괄호를 사용할 수 있습니다.");
                    System.out.println("3. 수식에 등호는 입력하지 않습니다.");
                    System.out.println("수식 예시: ");
                    System.out.println("1+2*3,    10-2*3,    100/4(20+10)");
                    System.out.println("저장된 결과를 보고 싶으면 check, 저장된 결과를 삭제하려면 del, 프로그램을 종료하려면 exit 를 입력하세요.");
                    continue;

                case "exit":
                    flag= false;
                    break;

                case "check":
                    while (true){
                        if (calculator.isListEmpty()){
                            System.out.println();
                            System.out.println("================================================");
                            System.out.println("현재 연산결과가 없습니다.\n 처음으로 돌아갑니다.");
                            break;
                        }
                        System.out.println();
                        System.out.println("================================================");
                        System.out.println("현재 연산결과는 "+ calculator.getSize()+ "개 있습니다.");
                        System.out.println("조회를 원하는 결과의 번호를 입력해주세요\n 1~ "+ calculator.getSize()+"\n 결과조회를 나가시려면 0을 입력하세요");

                        try{
                            int checkNum= sc.nextInt();

                            if (checkNum==0){
                                //무슨 이유인지는 모르겠는데, 연산결과조회 반복문이 닫힐 때 sc.nextInt()가 살아 남아있는 듯 하다.
                                //때문에 다음 반복문으로 넘어갈 때 sc.nextLine에서 입력을 받지 않고 바로 수식 확인으로 넘어가버린다.
                                //sc.nextLine()을 여기서 넣어서 미리 스캐너를 초기화해준다.
                                sc.nextLine();
                                break;
                            }
                            calculator.checkResult(checkNum);
                        }
                        catch (InputMismatchException e){
                            System.out.println();
                            System.out.println("올바른 숫자를 입력해주세요.");
                            sc.nextLine(); // Clear invalid input
                        }
                    }
                    break;

                case "del":
                    while (true){
                        if (calculator.isListEmpty()){
                            System.out.println();
                            System.out.println("================================================");
                            System.out.println("현재 연산결과가 없습니다.\n 처음으로 돌아갑니다.");
                            break;
                        }
                        System.out.println();
                        System.out.println("================================================");
                        System.out.println("현재 연산결과는 "+ calculator.getSize()+ "개 있습니다.");
                        System.out.println("삭제를 원하는 결과의 번호를 입력해주세요\n 1~ "+ calculator.getSize()+"\n 삭제를 나가시려면 0을 입력하세요.\n 전부 삭제하시려면 999을 입력하세요.");

                        try {
                            int checkNum = sc.nextInt();

                            if (checkNum == 0) {
                                System.out.println();
                                sc.nextLine();
                                break;
                            } else if (checkNum == 999) {
                                System.out.println("모든 결과를 삭제합니다.");
                                System.out.println();
                                calculator.clearComputeList();
                                sc.nextLine();
                                break;
                            } else {
                                calculator.deleteResult(checkNum);
                            }
                        } catch (InputMismatchException e) {
                            System.out.println("올바른 숫자를 입력해주세요.");
                            sc.nextLine(); // Clear invalid input
                        }
                    }
                    break;

                default:
                    System.out.println();
                    System.out.println("================================================");
                    System.out.println("입력받은 수식: "+ inputFormula);
                    try{
                        calculator.work(inputFormula);
                    }
                    catch (IllegalArgumentException e)
                    {
                        System.out.println("올바른 수식을 입력해주세요.");
                    }
            }
        }
        sc.close();
    }
}
//죽여줘.