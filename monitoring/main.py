from flask import Flask, request, jsonify
from requests import get

app = Flask(__name__)

worksflows = dict()

@app.route("/workflows", methods=["POST"])
def get_workflow_info():
    workflow = dict()
    workflow_info = dict()

    data = request.get_json()
    workflow_name = data.get("workflow")

    response = get(url=f"https://api.github.com/repos/eduardosantoshf/t-tracker/actions/workflows/{workflow_name}/runs").json()

    workflow["runs"] = response["total_count"]
    workflow["data"] = list()

    for run in response["workflow_runs"]:
        workflow["data"].append(run)

    worksflows[workflow_name] = workflow
    
    return worksflows

if __name__ == "__main__":
    app.run()