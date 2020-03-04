#pragma once
#include <string>

class PODLPacket
{
public:
	static const int MAXDATA_LEN = 255;
	static const char *header;
	static const int ID_OFFSET = 4;
	static const int ID_LEN = 4;
	static const int DATA_OFFSET = 8;
	static const int MD5_LEN = 16;

	typedef std::string string;
	static const char OK = '\00';
	static const char REJECT = '\01';
	static const char INVALID = '\02';

public:
	PODLPacket(const char *message, size_t len);
	bool valid();
	bool validMd5();
	bool compareTo(const string& passwd);

private:
	char id[4];
	size_t dataLength;
	char data[MAXDATA_LEN] = {0};
	bool validChecksum;
	bool validFormat;
};

