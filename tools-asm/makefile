
FILE=bootloader.ATmega2560.03E000

$(FILE).hex: $(FILE).asm
	bin\avrasm2 \
		-I bin/inc -i m2560def.inc \
		-fI \
		-d $(FILE).dbg -l $(FILE).lst -m $(FILE).map \
		-e $(FILE).eeprom -o $@ $<
	copy $@ ..

clean:
	-del $(FILE).dbg $(FILE).hex $(FILE).lst $(FILE).map