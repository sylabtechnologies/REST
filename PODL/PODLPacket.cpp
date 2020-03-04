#include <boost/uuid/detail/md5.hpp>
#include "PODLPacket.h"

#include <iostream>

void prnHexval(char *ptr)
{
	for (size_t i = 0; i < 16; i++)
		std::cout << std::hex << (int) *(ptr + i);
	std::cout << std::endl;
}


// have valid format and valid md5 flags
// assume payload is always non null

using boost::uuids::detail::md5;

const char *PODLPacket::header = "PODL";

inline bool equal(const void *string1, const void *string2, size_t len)
{
	return memcmp(string1, string2, len) == 0;
}

inline void copy(const void *from, void *to, size_t len)
{
	memcpy(to, from, len);
}

PODLPacket::PODLPacket(const char * message, size_t	len)
{
	validChecksum = true;
	validFormat = true;

	if (len < DATA_OFFSET + MD5_LEN)
	{
		validFormat = false; return;
	}

	dataLength = len - DATA_OFFSET - MD5_LEN;
	if (dataLength > MAXDATA_LEN)
	{
		validFormat = false; return;
	}

	if (!equal(message, header, ID_OFFSET))
	{
		validFormat = false; return;
	}

	copy(message + ID_OFFSET, id, ID_LEN);
	copy(message + DATA_OFFSET, data, dataLength);

	md5 hash;
	md5::digest_type digest;
	hash.process_bytes(data, dataLength);
	hash.get_digest(digest);

	char *ptr = const_cast<char*>(message) + DATA_OFFSET + dataLength;
	prnHexval(ptr);
	prnHexval((char*)digest);

	if (!equal(digest, message + DATA_OFFSET + dataLength, MD5_LEN))
		validChecksum = false;
}

bool PODLPacket::valid()
{
	return validFormat;
}

bool PODLPacket::validMd5()
{
	return validChecksum;
}

bool PODLPacket::compareTo(const string & passwd)
{
	if (passwd.length() != dataLength) return false;

	return equal(passwd.c_str(), data, dataLength);
}

