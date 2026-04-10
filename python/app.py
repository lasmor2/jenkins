from flask import Flask, render_template_string
app = Flask(__name__)

HTML_TEMPLATE = """
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Cloud with VarJosh</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            display: flex;
            justify-content: center;
            align-items: center;
        }
        .container {
            text-align: center;
            background: white;
            padding: 50px;
            border-radius: 10px;
            box-shadow: 0 10px 25px rgba(0, 0, 0, 0.2);
            max-width: 600px;
        }
        h1 {
            color: #333;
            margin-bottom: 20px;
            font-size: 2.5em;
        }
        .emoji {
            font-size: 3em;
            margin-bottom: 20px;
        }
        p {
            color: #666;
            font-size: 1.1em;
            line-height: 1.6;
            margin-bottom: 20px;
        }
        .info-box {
            background: #f0f4ff;
            padding: 20px;
            border-radius: 8px;
            margin-top: 20px;
            border-left: 4px solid #667eea;
        }
        .tech-stack {
            display: flex;
            justify-content: space-around;
            margin-top: 30px;
            flex-wrap: wrap;
        }
        .tech-item {
            background: #f0f4ff;
            padding: 15px 20px;
            margin: 5px;
            border-radius: 5px;
            color: #667eea;
            font-weight: bold;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="emoji">✨</div>
        <h1>Welcome to Cloud with VarJosh</h1>
        <p>Your Flask application is running successfully on Docker!</p>
        
        <div class="info-box">
            <strong>Technology Stack:</strong>
            <div class="tech-stack">
                <div class="tech-item">Flask</div>
                <div class="tech-item">Jenkins</div>
                <div class="tech-item">Docker</div>
                <div class="tech-item">Linux</div>
            </div>
        </div>
        
        <p style="margin-top: 30px; font-size: 0.9em; color: #999;">
            Built with Flask • Shipped by Jenkins • Running in Docker
        </p>
    </div>
</body>
</html>
"""

@app.get("/")
def hello():
    return render_template_string(HTML_TEMPLATE)

if __name__ == "__main__":
    app.run(host="0.0.0.0", port=5000)
