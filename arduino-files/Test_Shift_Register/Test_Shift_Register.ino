int ST_CP = 11;
int SH_CP = 10;
int DS = 9;
int OE = 3;
 
byte leds = 0;
 
void setup() 
{
  pinMode(ST_CP, OUTPUT);
  pinMode(SH_CP, OUTPUT);  
  pinMode(DS, OUTPUT);
  pinMode(OE, OUTPUT);
}
 
void loop() 
{
  leds = 254;
  updateShiftRegister();
  //delay(500);
  for (int i = 255; i >= 0; i--)
  {
    //bitSet(leds, i+1);
    //bitClear(leds, i);
    setBrightness(i);
    updateShiftRegister();
    delay(20);
  }
}
 
void updateShiftRegister()
{
   digitalWrite(ST_CP, LOW);
   shiftOut(DS, SH_CP, LSBFIRST, leds);
   digitalWrite(ST_CP, HIGH);
}

void setBrightness(byte bright)
{
  analogWrite(OE, 255-bright);
}

