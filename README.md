입력한 수식을 처리하는 계산기.\n
양의 정수 계산을 처리 가능함\n
객체지향 방식으로 처리함\n
받은 수식을 스택과 큐를 활용하여 후위 전환식으로 재구성후\n
후위전환식을 스택을 이용해 계산함.\n
\n
규칙\n
========================\n
다음과 같은 형식의 수식입력을 처리할 수 있음\n
12+3(4-5)/6+(7*8)\n
수식에서는 양의 값만 입력 가능하지만, 결과는 음수도 처리 가능.\n
수식에는 정수만 입력 가능함.\n
수식에 빈 칸이 있으면 자동으로 제거함.\n
현재 처리 가능한 연산은 덧셈(+), 뺄셈(-), 곱셈(*), 나눗셈(/), 모듈러(%)\n
단 결과 혹은 계산 과정중 소수가 연관될 경우 정수로 반올림함.\n
