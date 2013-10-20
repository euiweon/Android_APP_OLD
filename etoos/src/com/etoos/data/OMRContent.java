package com.etoos.data;


public class OMRContent 
{
	public Integer Number = 1 ; // 문제 번호
	public Integer 	QuestionType = 0 ;	// 문제 타입, 주관식인지 객관식인지. ( 객관식 0 , 주관식 1 )
	public Integer	AnswerCount = 2;	// 정답 갯수 ( 주관식일때는 항상  0 )
	public Integer []	Answer ={0,0,0,0,0};	// 정답  0 = NONE 1 = 예비 2 = 정답 3 = 정답  + 예비 
	public String 	AnswerString = "";	// 주관식 정답 
	public Integer 	AnswerStringState = 2;	// 주관식 마킹상태 
	public Boolean Later  = false;		// 나중에 풀기
	public Integer	refQuestion = -1; // 문제 참조 번호 (문제 보기 버튼을 눌렀을 경우 )
	public String Timer	= "0";	// 타이머 ... 재선택시 원래 있던 시간에서 추가해서 처리해야한다. 
	
	public  Integer []	AnswerCheck ={0,0,0,0,0}; // 정답  0 = NONE 1 = 체크
	public Integer 	AnswerCheck2 = 0; 
	
	public Integer Score = 2;
	
}
