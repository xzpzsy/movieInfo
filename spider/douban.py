import random
import os
import re
import socket
import telnetlib
import time
import json
import pymysql
import requests
from bs4 import BeautifulSoup
from PIL import Image
import requests
from lxml import etree

# 代理ip列表
proxy_url = "https://raw.githubusercontent.com/fate0/proxylist/master/proxy.list"
# 写入可用ip代理池文件路径
ip_pool_file = "verified_proxies.json"
# 用于测试代理ip是否可用的网站
test_url = "http://icanhazip.com/"

# user-agent头
USER_AGENTS = [
    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; AcooBrowser; .NET CLR 1.1.4322; .NET CLR 2.0.50727)",
    "Mozilla/5.0 (Windows; U; MSIE 9.0; Windows NT 9.0; en-US)",
    "Mozilla/5.0 (X11; U; Linux; en-US) AppleWebKit/527+ (KHTML, like Gecko, Safari/419.3) Arora/0.6",
    "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.8.1.2pre) Gecko/20070215 K-Ninja/2.1.1",
    "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9) Gecko/20080705 Firefox/3.0 Kapiko/3.0",
    "Mozilla/5.0 (X11; Linux i686; U;) Gecko/20070322 Kazehakase/0.4.5",
    "Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.0.8) Gecko Fedora/1.9.0.8-1.fc10 Kazehakase/0.5.6",
    "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.11 (KHTML, like Gecko) Chrome/17.0.963.56 Safari/535.11",
    "Opera/9.80 (Macintosh; Intel Mac OS X 10.6.8; U; fr) Presto/2.9.168 Version/11.52",
    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; QQDownload 732; .NET4.0C; .NET4.0E; LBBROWSER)",
    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; QQDownload 732; .NET4.0C; .NET4.0E)",
    "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; Trident/4.0; SV1; QQDownload 732; .NET4.0C; .NET4.0E; 360SE)",
    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; QQDownload 732; .NET4.0C; .NET4.0E)",
    "Mozilla/5.0 (Windows NT 5.1) AppleWebKit/537.1 (KHTML, like Gecko) Chrome/21.0.1180.89 Safari/537.1",
    "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.1 (KHTML, like Gecko) Chrome/21.0.1180.89 Safari/537.1",
    "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:2.0b13pre) Gecko/20110307 Firefox/4.0b13pre",
    "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:16.0) Gecko/20100101 Firefox/16.0",
    "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.64 Safari/537.11",
    "Mozilla/5.0 (X11; U; Linux x86_64; zh-CN; rv:1.9.2.10) Gecko/20100922 Ubuntu/10.10 (maverick) Firefox/3.6.10",
]


def concat_proxy(item):
    return '{}://{}'.format(*item)


# 从ip网站上爬取所有代理ip
def get_proxy(proxy):
    response = requests.get(proxy)
    proxy_list = response.text.split("\n")
    for proxy_str in proxy_list:
        proxy_json = json.loads(proxy_str)
        host = proxy_json["host"]
        port = proxy_json["port"]
        ip_type = proxy_json["type"]
        check_and_save_ip(host, port, ip_type)


def check(protocol, ip_port, timeout=1):
    protocol = protocol.lower()
    ip = ip_port.split(':')[0]
    port = ip_port.split(':')[1]
    try:
        telnetlib.Telnet(ip, port=port, timeout=timeout)
        return True
    except Exception as e:
        print(e)
        return False


# 测试ip是否可用，可用的话存入文件
def check_and_save_ip(ip, port, ip_type):
    proxies = {}
    try:
        # 测试是否能使用
        telnetlib.Telnet(ip, port=port, timeout=1)
    except socket.timeout as e:
        print(e)
    else:
        print('connected successfully')
        proxies['type'] = ip_type
        proxies['host'] = ip
        proxies['port'] = port
        proxies_json = json.dumps(proxies)
        with open(ip_pool_file, 'a+') as fp:
            fp.write(proxies_json + '\n')
        print("已写入：%s" % proxies)


# 随机获取一个UA头
def get_request_headers():
    header = {
        'User-Agent': random.choice(USER_AGENTS),
        'Accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8',
        'Connection': 'keep-alive',
        'Accept-Language': 'en-US,en;q=0.5',
    }
    return header


def construct_proxy_structure(protocol_type):
    proxies = []
    with open(ip_pool_file, "r") as fp:
        while True:
            item = fp.readline()
            if item:
                if json.loads(item)['type'] == protocol_type:
                    proxies.append(json.loads(item))
            else:
                break
    random.seed()
    proxy = proxies[random.randint(0, len(proxies) - 1)]
    ip = proxy['host']
    port = proxy['port']
    proxy_param = {
        protocol_type: '%s:%s' % (ip, port)
    }
    print(proxy_param)
    return proxy_param


def get_db_top100():
    start = 0
    top250_movies = list
    url = "http://movie.douban.com/top250?start="
    url = url + str(start)
    print(url)
    while start <= 225:
        try:
            response = requests.get(url=url, headers=get_request_headers(), proxies=construct_proxy_structure('http'),
                                    timeout=5)
            if response.ok:
                soup = BeautifulSoup(response.text, features="lxml")
                for each in soup.find_all('a'):
                    if each.find('img') is not None and 'class' in each.find('img').attrs:
                        top250_movies.append(each.attrs['href'].split('/')[4])
            start += 25
            time.sleep(3 + random.uniform(1, 3))
        except Exception as e:
            print(e)

    # top250_movies = ['1292052', '1291546', '1292720', '1295644', '1292722', '1292063', '1291561', '1295124', '3541415', '3011091', '1292001', '1889243', '1292064', '3793023', '2131459', '1291549', '1292213', '5912992', '25662329', '1307914', '1291841', '1291560', '1849031', '3319755', '6786002', '1292052', '1291546', '1292720', '1295644', '1292722', '1292063', '1291561', '1295124', '3541415', '3011091', '1292001', '1889243', '1292064', '3793023', '2131459', '1291549', '1292213', '5912992', '25662329', '1307914', '1291841', '1291560', '1849031', '3319755', '6786002', '1292052', '1291546', '1292720', '1295644', '1292722', '1292063', '1291561', '1295124', '3541415', '3011091', '1292001', '1889243', '1292064', '3793023', '2131459', '1291549', '1292213', '5912992', '25662329', '1307914', '1291841', '1291560', '1849031', '3319755', '6786002', '1292052', '1291546', '1292720', '1295644', '1292722', '1292063', '1291561', '1295124', '3541415', '3011091', '1292001', '1889243', '1292064', '3793023', '2131459', '1291549', '1292213', '5912992', '25662329', '1307914', '1291841', '1291560', '1849031', '3319755', '6786002', '1292052', '1291546', '1292720', '1295644', '1292722', '1292063', '1291561', '1295124', '3541415', '3011091', '1292001', '1889243', '1292064', '3793023', '2131459', '1291549', '1292213', '5912992', '25662329', '1307914', '1291841', '1291560', '1849031', '3319755', '6786002', '1292052', '1291546', '1292720', '1295644', '1292722', '1292063', '1291561', '1295124', '3541415', '3011091', '1292001', '1889243', '1292064', '3793023', '2131459', '1291549', '1292213', '5912992', '25662329', '1307914', '1291841', '1291560', '1849031', '3319755', '6786002', '1292052', '1291546', '1292720', '1295644', '1292722', '1292063', '1291561', '1295124', '3541415', '3011091', '1292001', '1889243', '1292064', '3793023', '2131459', '1291549', '1292213', '5912992', '25662329', '1307914', '1291841', '1291560', '1849031', '3319755', '6786002']
    for each in top250_movies():
        movie = dict()
        try:
            pass
        except Exception as e:
            print(e)

def reverse1(s):
 s=input("请输入需要反转的内容：")
 return s[::-1]


def get_movie_details(series, movie_id, video_type):
    print(video_type)
    movie = dict()
    directory = 'D://Project//MovieInfo//project//images//movie//'
    image_format = '.jpg'
    movie_type = ""
    re_date = re.compile(r'\d*-\d*-\d*.*')
    name = 'test'
    date = '1000-01-01'
    url = 'https://movie.douban.com/subject/%s/' % movie_id
    proxies = construct_proxy_structure('https')
    while not check('https', proxies['https']):
        proxies = construct_proxy_structure('https')
    try:
        time.sleep(random.uniform(1, 3))
        response_detail = requests.get(url=url,headers=get_request_headers())
        soup_detail = BeautifulSoup(response_detail.text, features="lxml")
        movie['name'] = soup_detail.find('title').string.replace(' ',"",8).replace('\n','')[::-1].replace(' ','',1)[::-1].split('(')[0]
        infors = soup_detail.find_all('span')
        for info in infors:
            if 'property' in info.attrs:
                if info.attrs.get('property') == 'v:genre':
                    movie_type += (info.string + " ")
                if info.attrs.get(
                        'property') == 'v:initialReleaseDate' and re_date.match(
                    info.string[:10]) is not None and date == '1000-01-01':
                    date = info.string[:10]
                if info.attrs.get('property') == 'v:summary':
                    introduction = info.text.replace('\n', '').replace(' ', '').replace(
                        '\u3000', '')
        img = soup_detail.find('img')
        origin_name = img.attrs.get('alt')
        movie['origin_name'] = origin_name
        movie['type'] = movie_type
        movie['date'] = date
        movie['introduction'] = introduction.replace("\"", "")
        movie['rating'] = soup_detail.find('strong').string
        movie['series'] = series
        movie['douban_url'] = url
        movie['address'] = 'D://OneDriver//OneDrive - 东南大学//%s//%s//'%(video_type,series) + movie['name']
        if img.attrs.get('title') == '点击看更多海报':
            time.sleep(3 + random.uniform(1, 3))
            final_img = requests.get(img.attrs.get('src'), headers=get_request_headers())
            with open(directory + movie['name'] + image_format, 'wb') as file:
                file.write(final_img.content)
            pic = Image.open(directory + movie['name'] + image_format)
            newpic = pic.resize((318, 450), Image.ANTIALIAS)
            print(directory + name +image_format)
            newpic.save(directory + movie['name'] + image_format)
    except Exception as e:
        print(e)
    print(movie)
    store_to_database(movie)
    time.sleep(random.uniform(1, 3))
    return True

def get_movie_information(movie_names, video_type):
    if len(movie_names) == 0:
        print('2123')
        return
    if video_type == '电影':
        video_type_name = 'movies'
    else:
        video_type_name = 'TV series'
    print(video_type)
    for series, names in movie_names.items():
        for name in names:
            # proxies = construct_proxy_structure('https')
            # while not check('https', proxies['https']):
            #     proxies = construct_proxy_structure('https')
            if filter_movie_names(name):
                url = 'https://www.douban.com/search?cat=1002&q=' + name
                # 对URL参数进行封装
                # 发起请求
                try:
                    response = requests.get(url=url, headers=get_request_headers(),
                                            timeout=5)
                    # 获取响应数据
                    print(response)
                    if response.ok:
                        soup = BeautifulSoup(response.text, features="lxml")
                        for h in soup.find_all('h3')[:5]:
                            if len(h.find_all('span')) >= 1:
                                if h.find('span').string == '[' + video_type + ']':
                                    each = h.find('a')
                                    if 'class' not in each.attrs and 'movie' in each.attrs.get(
                                            'onclick') and name == each.string[:-1]:

                                        if get_movie_details(series,re.compile(r'%2F(\d+)%2F').findall(each.attrs.get('href'))[0],video_type_name) :
                                            break

                except Exception as ex:
                    print(ex)


def filter_movie_names(file_name):
    try:
        db = pymysql.connect('localhost', 'root', 'zsyxzp06270314', 'movie')
        cursor = db.cursor()
        sql = 'select * from movies where name = "%s"' % file_name;
        cursor.execute(sql)
        cursor.close()
        db.commit()
        if cursor.fetchone() is None:
            return True
        else:
            return False
    except pymysql.Error as e:
        print(e)
    finally:  # 如果连接成功就要关闭数据库
        if db:
            db.close()


def store_to_database(movie):
    try:
        db = pymysql.connect('localhost', 'root', 'zsyxzp06270314', 'movie')
        cursor = db.cursor()
        sql = 'insert into movies (name,origin_name,type,rating,douban_url,introduction,date,address,series) values ("%s","%s","%s","%s","%s","%s","%s","%s","%s")' \
              % (movie['name'], movie['origin_name'], movie['type'], movie['rating'], movie['douban_url'],
                 movie['introduction'], movie['date'], movie['address'], movie['series'])
        print(sql)
        cursor.execute(sql)
        cursor.close()
        db.commit()
    except pymysql.Error as e:
        print(e)
    finally:  # 如果连接成功就要关闭数据库
        if db:
            db.close()


def get_movie_names(file_dir):
    series_movies = dict()
    for dirpath, dirnames, filenames in os.walk(file_dir):
        series = dirnames
        break
    for each in series:
        for dirpath, dirnames, filenames in os.walk(file_dir + "//" + each):
            series_movies.update({each: dirnames})
            break
    return series_movies


if __name__ == '__main__':
    # movies_names = get_movie_names("D://OneDriver//OneDrive - 东南大学//movies")
    # series_names = get_movie_names("D://OneDriver//OneDrive - 东南大学//TV series")
    # print(series_names)
    # get_movie_information(movies_names,'电影')
    # get_movie_information(series_names,'电视剧')
    # print("12132"+str(0))
    # get_proxy(proxy_url)
    # construct_proxy_structure('http')
    # print(check_proxy('92.244.99.229','3128','https'))
    # check('http','139.224.37.83','3128')
    # str = 'https://movie.douban.com/subject/1291546/'.split('/')[4]
    # print(str)
    # get_db_top100()
    # test()
    names = get_movie_names("D://OneDriver//OneDrive - 东南大学//TV series")
    print(names)
    get_movie_information(names,'电视剧')
