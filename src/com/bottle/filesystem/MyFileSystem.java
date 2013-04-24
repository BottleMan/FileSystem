package com.bottle.filesystem;

import java.util.*;
import java.io.*;

/**
 * 文件系统模拟
 * 
 * @author Bottle
 * 
 * @Date 2013-4-11 下午4:11:19
 */
public class MyFileSystem {
	static String data[][] = new String[100][7];
	static String userName[] = { "", "", "", "", "", "", "", "", "", "" };

	public static void main(String argv[]) throws IOException {
		System.out.println("欢迎使用文件系统模拟");
		int nameFlag = 0;
		String username1 = null;
		String judge = null;
		readname("UserName.txt");
		while (true) {
			InputStreamReader reader = new InputStreamReader(System.in);
			BufferedReader input = new BufferedReader(reader);
			System.out.print("输入用户名:");
			username1 = input.readLine();
			while (true) {
				int flag = 0;
				for (flag = 0; flag < 10; flag++) {
					if (username1.trim().equals(userName[flag])) {
						System.out.println("登录成功");
						commondFS(username1.trim());
						break;
					}
				}
				if (flag != 10)
					break;
				System.out.println("不存在的用户名");
				System.out.println("是否注册？yes进入 任意键退出");
				judge = input.readLine();
				if (judge.trim().equals("yes")) {
					userName[nameFlag] = username1.trim();
					for (int i = 0; i < 10; i++)
						System.out.println(userName[i]);
					System.out.println("你已经登录");
					writename("UserName.txt");
					commondFS(username1.trim());
					nameFlag++;
					break;
				} else {
					break;
				}
			}
		}//end of first while
	}//end of main

	/**
	 * 执行命令
	 * 
	 * @param username
	 * @throws IOException
	 */
	public static void commondFS(String username) throws IOException {
		String commond = null;
		String cmd[] = new String[3];
		String file[] = new String[7];
		int getNumber = 0, emptyNumber = 0;
		read("FileDisk.txt");
		while (true) {
			System.out.print(username + ":->");
			InputStreamReader reader = new InputStreamReader(System.in);
			BufferedReader input = new BufferedReader(reader);
			commond = input.readLine();
			StringTokenizer parser = new StringTokenizer(commond, " ");
			int s = 0;
			while (parser.hasMoreTokens()) {
				cmd[s] = parser.nextToken();
				s++;
			}
			if (cmd[0].trim().equals("dir")) {
				int m = 0;
				System.out.println("+---------+---------+---------+---------+-------------+---------+");
				System.out.println("|  文件名      |   用户名    |   地址       |  文件长度   |  只读1/可写2  |  打开控制  |");
				System.out.println("+---------+---------+---------+---------+-------------+---------+");
				for (int i = 0; i < 100; i++) {
					if (data[i][1] != null && data[i][1].equals(username)) {
						System.out.printf("|%9s|%9s|%9s|%9s|%13s|%9s|\n", data[i][0], data[i][1], data[i][2],
								data[i][3], data[i][4], data[i][5]);
						System.out.println("+---------+---------+---------+---------+-------------+---------+");
						m++;
					}
				}
				if (m == 0) {
					System.out.println("没有目录项");
				} else
					System.out.println("文件个数" + m);
			} else if (cmd[0].trim().equals("create")) {
				int f;
				for (f = 0; f < 100; f++) {
					if (data[f][5] != null && data[f][5].equals("open")) {
						System.out.println("有文件在内存中，请先关闭");
						break;
					}
				}
				if (f == 100) {
					int k;
					for (k = 0; k < 100; k++) {
						if (data[k][5] != null && data[k][5].equals("close")) {
							continue;
						} else {
							emptyNumber = k;
							break;
						}
					}
					System.out.println("请输入文件名");
					file[0] = input.readLine().trim();
					file[1] = username;
					for (int m = 0; m < 100; m++) {
						if (file[0].equals(data[m][0]))
							if (file[1].equals(data[m][1])) {
								System.out.println("文件名冲突，请重命名");
								file[0] = input.readLine().trim();
							}
					}
					file[2] = "address" + String.valueOf(emptyNumber); //physical address
					file[5] = "open"; //open contral
					while (true) {
						System.out.println("请设置读写属性(1.只读 2.可读写)");
						commond = input.readLine();
						if (commond.equals("1") || commond.equals("2")) {
							file[4] = commond.trim();
							break;
						}
					}
					System.out.println("文件" + file[0] + "已经打开");
					System.out.println(" 请输入文件初始数据");
					file[6] = input.readLine().trim();
					file[3] = String.valueOf(file[6].length());
					for (int j = 0; j < 7; j++) {
						data[emptyNumber][j] = file[j];
					}
					file[5] = "close";
					data[emptyNumber][5] = "close";
					System.out.println("文件" + file[0] + "已经关闭");
				}
			} else if (cmd[0].trim().equals("delete")) {

				for (int i = 0; i < 100; i++) {
					if (data[i][0] != null && data[i][0].equals(cmd[1])) {
						if (data[i][1].equals(username)) {
							data[i][1] = null;
							System.out.println("文件" + cmd[1] + "已经删除");
							break;
						}//end of if username
					} else {
						if (i == 99) {
							System.out.println("你无权删除该文件");
							break;
						}
					}
				}//end of for
			} else if (cmd[0].trim().equals("open")) {
				for (int i = 0; i < 100; i++) {
					if (data[i][5] != null && data[i][5].equals("open")) {
						System.out.println("有文件" + file[0] + "在内存中，请先关闭");
						break;
					} else {
						if (data[i][0] != null && data[i][0].equals(cmd[1])) {
							if (username.equals(data[i][1])) {
								getNumber = i;

								for (int n = 0; n < 7; n++) {
									file[n] = data[getNumber][n];
								}
								file[5] = "open";
								data[getNumber][5] = "open";
								System.out.println("file :" + cmd[1] + " 已经打开  ");
								break;
							}
						} else {
							if (i == 99) {
								System.out.println("文件不存在");
								break;
							}
						}
					}
				}//end of for()
			} else if (cmd[0].trim().equals("close")) {

				for (int n = 0; n < 7; n++) {
					data[getNumber][n] = file[n];
				}
				data[getNumber][5] = "close";
				file[5] = "close";
				System.out.println("文件" + file[0] + "已经关闭");

			} else if (cmd[0].trim().equals("read")) {
				for (int i = 0; i < 100; i++) {
					if (data[i][0] != null && data[i][0].equals(cmd[1])) {
						if (data[i][1].equals(username)) {
							for (int n = 0; n < 7; n++) {
								file[n] = data[i][n];
							}

							if (file[5].equals("close")) {
								System.out.println("文件没有打开！ ");
								break;
							} else if (file[5].equals("open")) {
								System.out.println(file[6]);
								break;
							}

						}
					}
				}

			} else if (cmd[0].trim().equals("write")) {
				String changeString = "";
				String chose = "";
				for (int i = 0; i < 100; i++) {
					if (data[i][0] != null && data[i][0].equals(cmd[1])) {
						if (data[i][1].equals(username)) {
							for (int n = 0; n < 7; n++) {
								file[n] = data[i][n];
							}

							if (file[5].equals("close")) {
								System.out.println("文件没有打开！ ");
								break;
							} else if (file[5].equals("open")) {
								if (file[4].equals("1")) {
									System.out.println("只读文件,不能改写");
									break;
								} else {
									System.out.println("输入修改数据");
									changeString = input.readLine();
									while (true) {
										System.out.println("修改选择：全部修改 1  增加数据 2");
										chose = input.readLine();
										if (chose.equals("1")) {
											file[6] = changeString;
											file[3] = String.valueOf(file[6].length());
											break;
										} else if (chose.equals("2")) {
											file[6] += changeString;
											break;
										} else
											continue;
									}
									System.out.println(" 修改后数据：");
									System.out.println(file[6]);
									file[3] = String.valueOf(file[6].length());

								}
							}
						}
					}
				}

			} else if (cmd[0].trim().equals("logout")) {
				int f;
				for (f = 0; f < 100; f++) {
					if (data[f][5] != null && data[f][5].equals("open")) {
						System.out.println("有文件在内存中，请先关闭");
						break;
					}
				}
				if (f == 100) {
					write("FileDisk.txt");
					System.out.println("用户" + username + "退出系统。 ");
					return;
				}

			} else if (cmd[0].trim().equals("help")) {
				System.out.println();
				System.out.print("create ");
				System.out.println("创建文件");
				System.out.print("dir    ");
				System.out.println("列目录文件");
				System.out.print("logout   ");
				System.out.println("退出");

				System.out.println("以下命令需加文件名");
				System.out.println("eg：open ***");
				System.out.print("open   ");
				System.out.println("打开文件");
				System.out.print("close  ");
				System.out.println("关闭文件");
				System.out.print("read   ");
				System.out.println("读文件");
				System.out.print("write  ");
				System.out.println("写文件");
				System.out.print("delete ");
				System.out.println("删除文件");
				System.out.println();

			} else {
				System.out.println("错误命令");
			}
		}//end of while(true)
	}//end of commondFS

	/**
	 * 写入文件
	 * 
	 * @param filename
	 */
	public static void write(String filename) {
		try {
			File file = new File(filename);
			FileWriter writeOut = new FileWriter(file);
			String writeString = "";
			for (int i = 0; i < 100; i++)
				for (int j = 0; j < 7; j++) {
					writeString += data[i][j] + "#";

				}
			writeOut.write(writeString);
			writeOut.close();
		} catch (IOException e) {
			String err = e.toString();
			System.out.println(err);
			System.out.println("保存文件错误....");
		}
	}

	/**
	 * 注册
	 * 
	 * @param usenamefile
	 */
	public static void writename(String usenamefile) {
		try {
			File file = new File(usenamefile);
			FileWriter writeOut = new FileWriter(file);

			String nameString = "";
			for (int j = 0; j < 10; j++) {
				nameString += userName[j] + "#";

			}
			writeOut.write(nameString);
			writeOut.close();
		} catch (IOException e) {
			String err = e.toString();
			System.out.println(err);
			System.out.println("保存文件错误....");
		}

	}

	/**
	 * 读入用户信息文件
	 * 
	 * @param usernamefile
	 */
	public static void readname(String usernamefile) {
		try {
			File file = new File(usernamefile);
			FileReader readIn = new FileReader(file);

			int length = (int) file.length();
			int readChars = 0;
			char[] content = new char[length];
			while (readIn.ready())
				readChars += readIn.read(content, readChars, length - readChars);

			readIn.close();

			String readNF = new String(content, 0, readChars);

			StringTokenizer parser = new StringTokenizer(readNF, "#");
			int n = 0;

			while (parser.hasMoreTokens()) {
				userName[n] = parser.nextToken();
				n++;

			}

		} catch (IOException e) {
			String err = e.toString();
			System.out.println(err);
			System.out.println("读入文件错误....");
		}

	}

	/**
	 * 读文件
	 * 
	 * @param filename
	 */
	public static void read(String filename) {

		try {
			File file = new File(filename);
			FileReader readIn = new FileReader(file);

			int length = (int) file.length();
			int readChars = 0;
			char[] content = new char[length];
			while (readIn.ready())
				readChars += readIn.read(content, readChars, length - readChars);

			readIn.close();

			String readFD = new String(content, 0, readChars);

			StringTokenizer parser = new StringTokenizer(readFD, "#");
			int n = 0;
			String temp[] = new String[900];
			while (parser.hasMoreTokens()) {
				temp[n] = parser.nextToken();
				n++;

			}
			for (int i = 0; i < 100; i++)
				for (int j = 0; j < 7; j++) {
					data[i][j] = temp[i * 7 + j];
				}

		} catch (IOException e) {
			String err = e.toString();
			System.out.println(err);
			System.out.println("读入文件错误....");
		}
	}

}
