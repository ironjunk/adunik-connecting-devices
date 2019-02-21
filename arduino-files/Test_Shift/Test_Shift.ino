int ST_CP = 11;
int SH_CP = 10;
int DS = 9;
 
byte leds = 0;
 
void setup() 
{
  pinMode(ST_CP, OUTPUT);
  pinMode(SH_CP, OUTPUT);  
  pinMode(DS, OUTPUT);
}
 
void loop() 
{
  leds = 0;
  updateShiftRegister();
}
 
void updateShiftRegister()
{
   digitalWrite(ST_CP, LOW);
   shiftOut(DS, SH_CP, LSBFIRST, leds);
   shiftOut(DS, SH_CP, LSBFIRST, leds);
   digitalWrite(ST_CP, HIGH);
}

