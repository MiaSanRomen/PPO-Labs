using System;
using System.Collections.Generic;
using System.Net;
using System.Net.Sockets;
using System.Text;
using System.Threading;

namespace Chat
{
    class Program
    {
        static string remoteAddress;
        static int remotePort = 8001;
        static int localPort = 8001;
        static string myName;
        static List<string> messages = new List<string>();

        static void Main(string[] args)
        {
            try
            {
                Console.Write("Enter the remote address to connect: ");
                remoteAddress = Console.ReadLine();

                Console.Write("Enter your name:");
                myName = Console.ReadLine();
                Thread receiveThread = new Thread(new ThreadStart(ReceiveMessage));
                receiveThread.Start();
                Console.WriteLine("Successfully connected to " + remoteAddress);
                SendMessage(); 
            }
            catch (Exception ex)
            {
                Console.WriteLine(ex.Message);
            }
        }
        private static void SendMessage()
        {
            UdpClient sender = new UdpClient(); 
            try
            {
                while (true)
                {
                    string message = Console.ReadLine(); 
                    DateTime date = DateTime.Now;
                    message = String.Format("{0} || {1} : {2}", date, myName, message);
                    messages.Add(message);
                    WriteChat();
                    byte[] data = Encoding.Unicode.GetBytes(message);
                    sender.Send(data, data.Length, remoteAddress, remotePort); 
                }
            }
            catch (Exception ex)
            {
                Console.WriteLine(ex.Message);
            }
            finally
            {
                sender.Close();
            }
        }

        private static void ReceiveMessage()
        {
            UdpClient receiver = new UdpClient(localPort); 
            IPEndPoint remoteIp = null; 
            try
            {
                while (true)
                {
                    byte[] data = receiver.Receive(ref remoteIp); 
                    string message = Encoding.Unicode.GetString(data);
                    messages.Add(message);
                    WriteChat();
                }
            }
            catch (Exception ex)
            {
                Console.WriteLine(ex.Message);
            }
            finally
            {
                receiver.Close();
            }
        }

        private static void WriteChat()
        {
            Console.Clear();
            foreach(var message in messages)
            {
                Console.WriteLine(message);
            }
        }
    }
}
