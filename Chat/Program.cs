using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Sockets;
using System.Text;
using System.Threading;
using chat;

namespace Chat
{
    class Program
    {
        static string remoteAddress;
        static int remotePort = 8001;
        static int localPort = 8001;
        static string myName;
        static List<MessageInOrder> messages = new List<MessageInOrder>();

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
                    string date = DateTime.Now.ToLongTimeString();
                    message = String.Format("{0} || {1} || {2} : {3}", messages.Count, date, myName, message);
                    MessageInOrder messageInOrder = new MessageInOrder(messages.Count, message);
                    messages.Add(messageInOrder);
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
                    if (message.IndexOf('|') == 0)
                    {
                        int index = Int32.Parse(message.Substring(1));
                        var resendMessage = messages.LastOrDefault(x => x.Nomber >= index);     //Will resend only lost message
                        UdpClient sender = new UdpClient();
                        try
                        {
                            byte[] dataSend = Encoding.Unicode.GetBytes(resendMessage.Message);
                            sender.Send(data, data.Length, remoteAddress, remotePort);
                        }
                        finally
                        {
                            sender.Close();
                        }
                        Console.WriteLine("Lost message was resend.");
                    }
                    else
                    {
                        RightOrder(message);
                        WriteChat();
                    }
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
            foreach (var message in messages)
            {
                Console.WriteLine(message.Message);
            }
        }

        private static void RightOrder(String message)
        {
            int sizeOfNumber = message.IndexOf('|');
            int index = Int32.Parse(message.Substring(0, sizeOfNumber - 1));
            messages.OrderBy(x => x.Nomber);
            for(int i=messages.LastOrDefault().Nomber; i<index; i++)    //Will go through indexes only from last recieved to new message
            {
                if (messages.Where(x => x.Nomber == i).Count() != 0)    //If list doesn't contain message with such index, 
                {                                                       //app will ask another client to resend only this message
                    UdpClient sender = new UdpClient();
                    try
                    {
                        string lostIndex = String.Format("|{0}", messages.LastOrDefault().Nomber++);
                        byte[] data = Encoding.Unicode.GetBytes(lostIndex);
                        sender.Send(data, data.Length, remoteAddress, remotePort);
                    }
                    finally
                    {
                        sender.Close();
                    }
                }
            }
            messages.Add(new MessageInOrder(index, message));       //Will save recieved message in any case
        }
    }
}
