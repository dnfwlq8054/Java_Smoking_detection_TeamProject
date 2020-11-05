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

#define BAUDRATE B9600              //�ø��� �ӵ� 9600
#define MODEMDEVICE "/dev/rfcomm0"  //�������� ���� ���ϰ��
#define CHANEL 1                    //ä�� 1 ���ٴ� ǥ��(��� ��)
#define TRUE 1
#define FALSE 0

void error_handling(char *message);     //���� �ڵ鷯
void * send_signal(void * cs);          //�۽� ������ �Լ�
void * bluetooth(void * bt);            //�������� ������

char buf[BUFSIZ], msg[BUFSIZ];          //���ۿ� �޽������� ������ ����
int sock;                               //������ �ϳ��� �ʿ������� ��������

int main(int argc, char * argv[])
{
    struct sockaddr_in serv_addr;       //���� ����ü
    struct termios bluetootho, oldtio;  //�������� ����ü	
    int fd;			//���� ��ũ���� ���� ����

    pthread_t send_thread, bluetooth_thread;    //������ ����
    void * thread_run;                          //join����

    fd = open( MODEMDEVICE, O_RDWR | O_NOCTTY );    //������ ��ο��� ���� ����
    if(fd < 0) {perror(MODEMDEVICE); return -1; }

    tcgetattr(fd, &oldtio);
    //������ ������ ���� �����ϱ����� ����� ���ÿ� fd�� ����Ű�� �͹̳������� termios����ü�� ����

    memset(&bluetootho, 0, sizeof(bluetootho));

    bluetootho.c_cflag = BAUDRATE | CS8 | CLOCAL | CREAD;
    //������ �÷��� ���ۼӵ�, 8N1(8bit, no parity, 1stop bit)
    bluetootho.c_iflag = IGNPAR | IGNBRK;   //�Է¸�� �÷���, �丮Ƽ, ������ �˻�� ���� ����
    bluetootho.c_oflag = OPOST;             //��¸�� �÷���
    bluetootho.c_lflag = 0;                 //�Է� ���� �÷���
    bluetootho.c_cc[VMIN] = 1;              //cc�� ����ڵ�
    bluetootho.c_cc[VTIME] = 0;

    tcflush(fd, TCIFLUSH);                  //�����ִ� �� ��� ���� ����
    tcsetattr(fd, TCSANOW, &bluetootho);    //��� �� ����, fd�͹̳� ���� ����ü�� ����

    if(argc != 3)
    {
        printf("Usage : %s <IP> <PORT> \n", argv[0]);
        exit(1);
    }

    sock = socket(PF_INET, SOCK_STREAM, 0);//IPv4, TCP, 0
    if(sock == -1)
        error_handling("socket() error");

    memset(&serv_addr, 0, sizeof(serv_addr)); //sin_zero[8]�������� 0���� �ʱ�ȭ���ٷ���
    serv_addr.sin_family = AF_INET;		//IPv4
    serv_addr.sin_addr.s_addr = inet_addr(argv[1]); //IP�ּ� �Է�
    serv_addr.sin_port = htons(atoi(argv[2]));//short �������� ȣ��Ʈ -> ��Ʈ��ũ ����Ʈ�� ��ȯ
                                              //Big-endian, PORT��ȣ �Է�

    if(connect(sock, (struct sockaddr*)&serv_addr, sizeof(serv_addr)) == -1)	//������ ����
        error_handling("read() error!");

    pthread_create(&send_thread, NULL, send_signal, (void *)sock);  //�۽� ������ ����
                   //Thread �ĺ� ��, �ɼ�, ������ �Լ�, ������ ��
    pthread_create(&bluetooth_thread, NULL, bluetooth, (void *)fd); //�������� ������ ����
    pthread_join(send_thread, &thread_run);                         //�������� ���Ḧ ��ٷ��ش�.
    pthread_join(bluetooth_thread, &thread_run);
    close(sock);
    return 0;
}

void * send_signal(void * cs)	//������ �޽����� �۽��ϱ����� �Լ�
{
    int sock = (int)cs;
    char send_msg[BUFSIZ];

    while(1)
    {
        fgets(send_msg, 100, stdin); //���� �Է�(���߿� �ڵ����� �ѱ⵵�� ����)
        if(!strcmp(send_msg, "q\n"))
        {
            write(sock, -1, 1);    //�޽��� �۽�
            close(sock);
            exit(0);
        }

        write(sock, send_msg, strlen(send_msg));    //�޽��� �۽�
    }

}

void * bluetooth(void * bt)	//�Ƶ��̳� ���������� ���� ���۵� ���ڿ��� ó���ϴ� �Լ�
{
    int len = 0;
    int n, c = 1;
    int fd = (int) bt;
    char *dong = "A402-";   //�� �Է�
    char *ho;               //ȣ ���� ����
    char cp[100];	    //strtok�� ����� �� msg������ ����� ���� �迭

    while((n = read(fd, buf, sizeof(buf))) != 0)
    {
        printf("buf : %s\n", buf);
        buf[n] = '\0';
        sprintf(msg,"%s%s", msg, buf);
        len += n;

        if(buf[n - 1] == '\0')      //�Ƶ��̳뿡�� �Ѿ���� �������� ����Ǳ⵵ ����
        {                           //��µǴ� ���� ����
            if(c == TRUE)           //ȣ �� �����ϱ����� if��(1���� �����)
            {
                strcpy(cp, msg);
                ho = strtok(cp, "-");
                c = FALSE;
            }

            printf("fd : %d\n", fd);
            sprintf(msg, "%s-\n", msg);       //�ڹ� ������ ���� ���� ���� "-"�� �ڹټ������� ��ū��
         // printf("%s\n", msg);
            write(sock, dong, strlen(dong));
            write(sock, msg, strlen(msg));    //�޽��� �۽�

            memset(&msg, 0, sizeof(msg));
            len = 0;
        }

        printf("msg : %s\n", msg);
        if(buf[0] > 127)            //���������� ����� buf�� ������ ������ ä������ ���� Ȯ��
        {                           //������ ���� ������ ����ɷ� ����
            sprintf(msg, "%s%s-0-\n", dong, ho);	//���� ��� �ش� ��, ȣ ������ 0�� ����
            write(sock, msg, strlen(msg));
            printf("�������� ����\n");
            memset(&msg, 0, sizeof(msg));
        }
    }
}

void error_handling(char *message)      //���� �޽��� �ڵ鷯
{
    fputs(message, stderr);
    fputc('\n', stderr);
    exit(1);
}
