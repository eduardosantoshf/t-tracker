from flask import Flask, request, jsonify
from requests import get

app = Flask(__name__)

worksflows = dict()

@app.route("/workflows", methods=["POST"])
def get_workflow_info():
    workflow = dict()
    workflow_info = dict()
    error_dic = dict()

    data = request.get_json()

    workflow_name = data.get("workflow")
    page_number = data.get("page_number")

    response = get(url=f"https://api.github.com/repos/eduardosantoshf/t-tracker/actions/workflows/{workflow_name}/runs?per_page=5&page={page_number}")

    if response.status_code != 200: 
        print("Error on GitHub's response")
        return error_dic

    response_json = response.json()
    
    workflow["runs"] = response_json["total_count"]
    workflow["data"] = list()

    for run in response_json["workflow_runs"]:
        r = dict()

        r["id"] = run["id"]
        r["name"] = run["name"]
        r["event"] = run["event"]
        r["status"] = run["status"]
        r["conclusion"] = run["conclusion"]
        r["pull_requests"] = run["pull_requests"]
        r["created_at"] = run["created_at"]
        r["updated_at"] = run["updated_at"]
        r["head_commit"] = run["head_commit"]

        run_id = run["id"]

        response2 = get(url=f"https://api.github.com/repos/eduardosantoshf/t-tracker/actions/runs/{run_id}/timing")

        if response2.status_code != 200: 
            print("Error on GitHub's response")
            return error_dic

        response_json2 = response2.json()

        r["duration"] = response_json2["run_duration_ms"]

        workflow["data"].append(r)

    worksflows[workflow_name] = workflow
    
    return worksflows

if __name__ == "__main__":
    app.run(host='0.0.0.0')
