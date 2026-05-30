import os, yt_dlp

def download(username, download_dir):
    out_dir = os.path.join(download_dir, username)
    os.makedirs(out_dir, exist_ok=True)
    try:
        ydl_opts = {
            'outtmpl': os.path.join(out_dir, '%(title).80s_%(id)s.%(ext)s'),
            'quiet': True,
            'no_warnings': True,
            'ignoreerrors': True,
        }
        with yt_dlp.YoutubeDL(ydl_opts) as ydl:
            ydl.download([f'https://www.tiktok.com/@{username}'])
        return f"{username}: OK"
    except Exception as e:
        return f"{username}: LOI - {e}"
