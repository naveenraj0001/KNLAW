import requests
import bs4
import os

from urllib.parse import urlparse, unquote
from bs4 import BeautifulSoup
from pathlib import Path

BASE_URL = "https://www.mha.gov.in"
PDFS_URL = f"{BASE_URL}/en/acts?page={{}}"

PDFS_DIR = Path(__file__).parent.parent / "pdfs"


def get_file_from_url(url):
    parsed = urlparse(url)
    filename = os.path.basename(parsed.path)
    filename = unquote(filename)
    return filename

def download_page(page: int):
    soup = BeautifulSoup(requests.get(PDFS_URL.format(page)).text)
    view = soup.find("div", {"class": "view-content"})
    if not isinstance(view, bs4.Tag):
        return

    for tag in view.find_all("h3"):
        if not isinstance(tag, bs4.Tag):
            return
        sib = tag.find_next_sibling("div")
        if not isinstance(sib, bs4.Tag):
            return
        table = sib.find("table")
        if not isinstance(table, bs4.Tag):
            return

        folder_path = PDFS_DIR / Path(tag.text)
        for link in table.find_all("a"):
            if not isinstance(link, bs4.Tag):
                continue

            if not folder_path.is_dir():
                folder_path.mkdir()

            sub_link = str(link["href"])
            file_name = get_file_from_url(sub_link)
            if file_name.endswith(".PDF"):
                file_name = os.path.splitext(file_name)[0] + ".pdf"
            file_path = folder_path / file_name

            if file_path.is_file():
                continue

            with open(file_path, "wb") as f:
                f.write(requests.get(BASE_URL + sub_link).content)



def main():
    for i in range(3):
        download_page(i)
        print(f"Downloaded Page: {i}")


if __name__ == "__main__":
    main()
