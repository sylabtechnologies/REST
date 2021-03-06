// PODLServer.cpp
// https://stackoverflow.com/questions/55070320/how-to-calculate-md5-of-a-file-using-boost

#include <iostream>
#include <boost/asio.hpp>
#include "PODLPacket.h"

typedef boost::asio::io_service IOService;
typedef boost::asio::ip::udp::endpoint Endpoint;
typedef boost::asio::ip::udp::socket   Socket;

const int myPort = 10000;

inline void send_back(Socket &socket, Endpoint &ep, char c)
{
	char msg[1] = { c };
	socket.send_to(boost::asio::buffer(msg, 1), ep);
}

int main()
{
	IOService service;
	Endpoint myEndpoint(boost::asio::ip::udp::v4(), myPort), ep;
	Socket socket(service, myEndpoint);

	PODLPacket::string passwd = "passwd";

	while (true)
	{
		try
		{
			char msg[1024];
			auto recvd = socket.receive_from(boost::asio::buffer(msg, sizeof(msg)), ep);

			PODLPacket packet(msg, recvd);

			if (!packet.valid())
			{
				send_back(socket, ep, PODLPacket::INVALID);
			}
			else if (!packet.validMd5())
			{
				send_back(socket, ep, PODLPacket::INVALID);
			}
			else if (!packet.compareTo(passwd))
			{
				send_back(socket, ep, PODLPacket::REJECT);
			}
			else
			{
				send_back(socket, ep, PODLPacket::OK);
			}
		}
		catch (std::exception& e)
		{
			std::cout << e.what() << std::endl;
		}
	}

    return 0;
}

