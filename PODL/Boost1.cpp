/*

# make simple sync server

whereis boost
dpkg boost
wget https://raw.githubusercontent.com/sylabtechnologies/demo_DP/master/leetcode/....

*/

#include "stdafx.h"
#include <iostream>
#include <vector>
#include <boost/asio.hpp>
using boost::asio::io_service;
using boost::asio::buffer;
using boost::asio::ip::udp;

int main(int argc, char* argv[])
{
	// the book! p9 simple udp server
	// https://www.geeksforgeeks.org/synchronous-chatting-application-using-c-boostasio/

	// common! BEST BOOK:https://docplayer.net/53966455-Learning-boost-c-libraries-by-arindam-mukherjee-download-ebook-learning-boost-c-libraries-by-arindam-mukherjee-pdf.html
	// ie 123 works just get oo objects
    // https://books.google.com/books?id=G6JNCgAAQBAJ&pg=PA461&lpg=PA461&dq=boost+synchronous+%22udp+server%22&source=bl&ots=3jN-vtxWsx&sig=ACfU3U1-l2p5pPEl8BLAQnfNpEs2YzOHSQ&hl=en&sa=X&ved=2ahUKEwi9w9nHk_XnAhXilXIEHTxJC-wQ6AEwAnoECAoQAQ#v=onepage&q=boost%20synchronous%20%22udp%20server%22&f=false

	io_service service;
	udp::endpoint myEndpt(udp::v4(), 1111), ep;
	udp::socket sock(service, myEndpt);

	while (true)
	{
		try
		{
			char msg[256];
			auto recvd = sock.receive_from(buffer(msg, sizeof(msg)), ep);
			std::string packet(msg, msg + recvd);
			std::cout << "Received " << packet << std::endl;
		}
		catch (std::exception& e)
		{
			std::cout << e.what() << std::endl;
		}

			
	}

	return 0;
}

/* TODO - 123HACK, FOLLOW JAVA THINKING 1 *by *1

5. examples: this is good work on wrappers, by folliwing Java conventions?

MENTALLY WRAP EVERYTHING
https://gist.github.com/kaimallea/e112f5c22fe8ca6dc627

5.1. obviously beej is the dude
https://beej.us/guide/bgnet/

5.2 SEEEMS GOOD *** *** *** MUST OO CONQUER STRATEGICALLY
< ie TOP DOWN TOP DOWN
https://stackoverflow.com/questions/44273599/udp-communication-using-c-boost-asio

7. client really OK
	boost::asio::io_service io_service;
	UDPClient client(io_service, "localhost", "1111");
	client.send("Hello, World!");

*/
