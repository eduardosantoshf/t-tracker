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
    page_number = data.get("page_number")

    response = get(url=f"https://api.github.com/repos/eduardosantoshf/t-tracker/actions/workflows/{workflow_name}/runs?page={page_number}").json()

    workflow["runs"] = response["total_count"]
    workflow["data"] = list()

    for run in response["workflow_runs"]:
        #workflow["data"].append(run)
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

        response2 = get(url=f"https://api.github.com/repos/eduardosantoshf/t-tracker/actions/runs/{run_id}/timing").json()

        r["duration"] = response2["run_duration_ms"]

        workflow["data"].append(r)

    worksflows[workflow_name] = workflow
    
    return worksflows

if __name__ == "__main__":
    app.run()