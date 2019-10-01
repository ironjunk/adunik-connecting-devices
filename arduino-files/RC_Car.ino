// this is the script for Simple RC Car movements.

// defining pins for the L298N Driver inputs
#define A1 8
#define A2 9
#define APWM 5

#define B1 10
#define B2 11
#define BPWM 6

int status;

void setup() 
{
  Serial.begin(9600);
  pinMode(A1,OUTPUT);
  pinMode(A2,OUTPUT);
  pinMode(APWM,OUTPUT);
  pinMode(B1,OUTPUT);
  pinMode(B2,OUTPUT);
  pinMode(BPWM,OUTPUT);
}

void loop()
{

  // run the program till the bluetooth connection is active
  while(Serial.available() > 0)
  {
    
    // read the value coming in from the Serial connection
    int dir = Serial.parseInt();
    
    if(Serial.read() == '\n')
    {
      if(dir == 0) //forward
      {
        status = 0;
        digitalWrite(A1,HIGH);
        digitalWrite(A2,LOW);
        digitalWrite(B1,HIGH);
        digitalWrite(B2,LOW);
        analogWrite(APWM,250);
        analogWrite(BPWM,250);
      }
      else if(dir == 1) //reverse
      {
        status = 1;
        digitalWrite(A1,LOW);
        digitalWrite(A2,HIGH);
        digitalWrite(B1,LOW);
        digitalWrite(B2,HIGH);
        analogWrite(APWM,250);
        analogWrite(BPWM,250);
      }
      else if(dir == 2) //left
      {
        if(status == 0) //forward
        {
          digitalWrite(A1,HIGH);
          digitalWrite(A2,LOW);
          digitalWrite(B1,LOW);
          digitalWrite(B2,HIGH);
        }
        else if(status == 1) //reverse
        {
          digitalWrite(A1,LOW);
          digitalWrite(A2,HIGH);
          digitalWrite(B1,HIGH);
          digitalWrite(B2,LOW);
        }
        analogWrite(APWM,190);
        analogWrite(BPWM,100);
      }
      else if(dir == 3) //right
      {
        if(status == 0) //forward
        {
          digitalWrite(A1,LOW);
          digitalWrite(A2,HIGH);
          digitalWrite(B1,HIGH);
          digitalWrite(B2,LOW);
        }
        else if(status == 1) //reverse
        {
          digitalWrite(A1,HIGH);
          digitalWrite(A2,LOW);
          digitalWrite(B1,LOW);
          digitalWrite(B2,HIGH);
        }
        analogWrite(APWM,100);
        analogWrite(BPWM,190);
      }
      else if(dir == 4)
      {
        digitalWrite(A1,LOW);
        digitalWrite(A2,LOW);
        digitalWrite(B1,LOW);
        digitalWrite(B2,LOW);
      }
    }
  }
}
