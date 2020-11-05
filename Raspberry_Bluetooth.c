#include<stdio.h>
#include<stdlib.h>
#include<string.h>
#include<unistd.h>
#include<arpa/inet.h>
#include<sys/socket.h>
#include<sys/wait.h>
#include<sys/types.h>
#include<fcntl.h>
#include<termios.h>
#include<pthread.h>

#define BAUDRATE B9600              //시리얼 속도 9600
#define MODEMDEVICE "/dev/rfcomm0"  //블루투스 버퍼 파일경로
#define CHANEL 1                    //채널 1 쓴다는 표시(없어도 됨)
#define TRUE 1
#define FALSE 0

void error_handling(char *message);     //에러 핸들러
void * send_signal(void * cs);          //송신 쓰레드 함수
void * bluetooth(void * bt);            //블루투스 쓰레드

char buf[BUFSIZ], msg[BUFSIZ];          //버퍼와 메시지담을 변수를 선언
int sock;                               //소켓은 하나만 필요함으로 전역변수

int main(int argc, char * argv[])
{
    struct sockaddr_in serv_addr;       //소켓 구조체
    struct termios bluetootho, oldtio;  //블루투스 구조체	
    int fd;			//파일 디스크립터 저장 변수

    pthread_t send_thread, bluetooth_thread;    //쓰레드 변수
    void * thread_run;                          //join변수

    fd = open( MODEMDEVICE, O_RDWR | O_NOCTTY );    //지정된 경로에서 파일 오픈
    if(fd < 0) {perror(MODEMDEVICE); return -1; }

    tcgetattr(fd, &oldtio);
    //마지막 설정된 값을 복원하기위한 백업과 동시에 fd가 가르키는 터미널정보를 termios구조체에 적제

    memset(&bluetootho, 0, sizeof(bluetootho));

    bluetootho.c_cflag = BAUDRATE | CS8 | CLOCAL | CREAD;
    //제어모드 플레그 전송속도, 8N1(8bit, no parity, 1stop bit)
    bluetootho.c_iflag = IGNPAR | IGNBRK;   //입력모드 플레그, 페리티, 블록합 검사시 에러 무시
    bluetootho.c_oflag = OPOST;             //출력모드 플레그
    bluetootho.c_lflag = 0;                 //입력 가능 플레그
    bluetootho.c_cc[VMIN] = 1;              //cc는 제어문자들
    bluetootho.c_cc[VTIME] = 0;

    tcflush(fd, TCIFLUSH);                  //남아있는 입 출력 내용 지움
    tcsetattr(fd, TCSANOW, &bluetootho);    //즉시 값 변경, fd터미널 정보 구조체에 적제

    if(argc != 3)
    {
        printf("Usage : %s <IP> <PORT> \n", argv[0]);
        exit(1);
    }

    sock = socket(PF_INET, SOCK_STREAM, 0);//IPv4, TCP, 0
    if(sock == -1)
        error_handling("socket() error");

    memset(&serv_addr, 0, sizeof(serv_addr)); //sin_zero[8]베열값을 0으로 초기화해줄려고
    serv_addr.sin_family = AF_INET;		//IPv4
    serv_addr.sin_addr.s_addr = inet_addr(argv[1]); //IP주소 입력
    serv_addr.sin_port = htons(atoi(argv[2]));//short 데이터인 호스트 -> 네트워크 바이트로 변환
                                              //Big-endian, PORT번호 입력

    if(connect(sock, (struct sockaddr*)&serv_addr, sizeof(serv_addr)) == -1)	//서버와 연결
        error_handling("read() error!");

    pthread_create(&send_thread, NULL, send_signal, (void *)sock);  //송신 쓰레드 생성
                   //Thread 식별 값, 옵션, 실행할 함수, 전달할 값
    pthread_create(&bluetooth_thread, NULL, bluetooth, (void *)fd); //블루투스 쓰레드 생성
    pthread_join(send_thread, &thread_run);                         //쓰레드의 종료를 기다려준다.
    pthread_join(bluetooth_thread, &thread_run);
    close(sock);
    return 0;
}

void * send_signal(void * cs)	//서버에 메시지를 송신하기위한 함수
{
    int sock = (int)cs;
    char send_msg[BUFSIZ];

    while(1)
    {
        fgets(send_msg, 100, stdin); //문자 입력(나중에 자동으로 넘기도록 수정)
        if(!strcmp(send_msg, "q\n"))
        {
            write(sock, -1, 1);    //메시지 송신
            close(sock);
            exit(0);
        }

        write(sock, send_msg, strlen(send_msg));    //메시지 송신
    }

}

void * bluetooth(void * bt)	//아두이노 블루투스로 부터 전송된 문자열을 처리하는 함수
{
    int len = 0;
    int n, c = 1;
    int fd = (int) bt;
    char *dong = "A402-";   //동 입력
    char *ho;               //호 저장 변수
    char cp[100];	    //strtok를 사용할 때 msg내용을 백업해 놓는 배열

    while((n = read(fd, buf, sizeof(buf))) != 0)
    {
        printf("buf : %s\n", buf);
        buf[n] = '\0';
        sprintf(msg,"%s%s", msg, buf);
        len += n;

        if(buf[n - 1] == '\0')      //아두이노에서 넘어오는 정보들이 저장되기도 전에
        {                           //출력되는 것을 방지
            if(c == TRUE)           //호 를 저장하기위한 if문(1번만 실행됨)
            {
                strcpy(cp, msg);
                ho = strtok(cp, "-");
                c = FALSE;
            }

            printf("fd : %d\n", fd);
            sprintf(msg, "%s-\n", msg);       //자바 서버로 보낼 문자 조합 "-"로 자바서버에서 토큰함
         // printf("%s\n", msg);
            write(sock, dong, strlen(dong));
            write(sock, msg, strlen(msg));    //메시지 송신

            memset(&msg, 0, sizeof(msg));
            len = 0;
        }

        printf("msg : %s\n", msg);
        if(buf[0] > 127)            //블루투스가 끊기면 buf에 쓰레기 값들이 채워지는 것을 확인
        {                           //쓰레기 값이 나오면 끊긴걸로 간주
            sprintf(msg, "%s%s-0-\n", dong, ho);	//끊긴 경우 해당 동, 호 정보와 0을 보냄
            write(sock, msg, strlen(msg));
            printf("블루투스 끊김\n");
            memset(&msg, 0, sizeof(msg));
        }
    }
}

void error_handling(char *message)      //에러 메시지 핸들러
{
    fputs(message, stderr);
    fputc('\n', stderr);
    exit(1);
}
