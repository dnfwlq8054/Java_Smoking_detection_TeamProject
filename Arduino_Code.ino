#define LOSS 0
#define NOMAL 1
#define SINS 2

const int bluetoothTX = 2;	//수신
const int bluetoothRX = 3;	//송신
cost int gas = A0;		//A0 포트 사용
char ho[] = "2102-";	//아파트 호

void loop()
{
    Serial.println(analogRead(gas));

    if(analogRead(gas)> 400)    //감지 되면
    {
        bluetooth.print(ho);
        bluetooth.print(SINS);
        bluetooth.print('\0');
        delay(5000);
    }

    if(analogRead(gas) <= 400)  //평상시라면
    {    
        bluetooth.print(ho);
        bluetooth.print(NOMAL);
        bluetooth.print('\0');
        delay(5000); 
    }
}