// Tailles d'entrée n soumises au test (axe X)
const n = [
  200, 250, 300, 350, 400, 450, 500, 550, 600, 650, 700, 750, 800, 850, 900,
  950, 1000, 1050, 1100, 1150, 1200, 1250, 1300, 1350, 1400, 1450, 1500, 1550,
  1600, 1650, 1700, 1750, 1800, 1850, 1900, 1950, 2000, 2050, 2100, 2150, 2200,
  2250, 2300, 2350, 2400, 2450, 2500, 2550, 2600,
];

// Temps d'exécution moyens constatés en ms (axe Y)
const insertionTime = [
  0, 0, 1, 2, 0, 1, 2, 2, 7, 2, 3, 3, 8, 5, 5, 4, 4, 5, 4, 4, 5, 8, 8, 6, 7, 7,
  6, 8, 7, 8, 10, 16, 11, 8, 16, 10, 10, 8, 10, 10, 16, 13, 8, 12, 15, 12, 14,
  8, 15,
];
const mergeTime = [
  0, 0, 0, 0, 1, 1, 1, 0, 1, 0, 0, 1, 0, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1,
  1, 1, 2, 2, 1, 2, 1, 1, 1, 2, 1, 2, 1, 2, 1, 1, 1, 1, 0, 2, 1, 1, 1,
];
const nativeTime = [
  1, 0, 0, 0, 0, 1, 0, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 2, 1, 1, 1, 2, 1, 2,
  2, 2, 2, 1, 1, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 1, 2, 2, 1, 1, 2, 1, 2,
];

// Construction dynamique de la charte empirique (via Chart.js)
const ctx = document.getElementById("timeChart").getContext("2d");
new Chart(ctx, {
  type: "line",
  data: {
    labels: n,
    datasets: [
      {
        label: "Tri par Insertion => O(n²)",
        data: insertionTime,
        borderColor: "#ef4444",
        backgroundColor: "transparent",
        borderWidth: 3,
        tension: 0.1,
      },
      {
        label: "Tri par Fusion => O(n log n)",
        data: mergeTime,
        borderColor: "#2563eb",
        backgroundColor: "transparent",
        borderWidth: 3,
        tension: 0.1,
      },
      {
        label: "Tri par Arrays.sort() => O(n log n)",
        data: nativeTime,
        borderColor: "#10b981",
        backgroundColor: "transparent",
        borderWidth: 3,
        tension: 0.1,
      },
    ],
  },
  options: {
    responsive: true,
    maintainAspectRatio: false,
    scales: {
      x: {
        type: "linear",
        title: {
          display: true,
          text: "Taille réelle du problème (n = nbre de bâtiments)",
          font: { weight: "bold" },
        },
      },
      y: {
        title: {
          display: true,
          text: "Temps d'exécution constaté (en ms)",
          font: { weight: "bold" },
        },
        beginAtZero: true,
      },
    },
  },
});
