import csv
from pathlib import Path
import matplotlib.pyplot as plt

results = Path('results')
with (results / 'results.csv').open(newline='') as file:
    rows = list(csv.DictReader(file))


def times(algorithm, scenario):
    data = [row for row in rows if row['algorithm'] == algorithm and row['scenario'] == scenario]
    data.sort(key=lambda row: int(row['input_size']))
    return [int(row['input_size']) for row in data], [float(row['avg_time_ms']) for row in data]


plt.figure(figsize=(8, 5))
for algorithm in ('LCS', 'Edit Distance'):
    x, y = times(algorithm, 'random')
    plt.plot(x, y, marker='o', label=algorithm)
plt.xlabel('Length of each input string')
plt.ylabel('Average execution time (ms)')
plt.title('Dynamic programming: execution time')
plt.grid(alpha=0.3)
plt.legend()
plt.tight_layout()
plt.savefig(results / 'dp_times.png', dpi=160)
plt.close()

fig, axes = plt.subplots(1, 2, figsize=(12, 4.5))
for ax, scenario in zip(axes, ('present', 'absent')):
    for algorithm in ('Naive', 'KMP', 'Rabin-Karp'):
        x, y = times(algorithm, scenario)
        ax.plot(x, y, marker='o', label=algorithm)
    ax.set_xlabel('Text length')
    ax.set_ylabel('Average execution time (ms)')
    ax.set_title(f'Pattern {scenario}')
    ax.grid(alpha=0.3)
    ax.legend()
fig.suptitle('String matching: execution time')
fig.tight_layout()
fig.savefig(results / 'matching_times.png', dpi=160)
plt.close(fig)

print('Saved results/dp_times.png and results/matching_times.png')
