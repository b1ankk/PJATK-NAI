#include <iostream>
#include <fstream>
#include <bitset>
#include <array>
#include <chrono>
#include <vector>
#include <future>
#include <cmath>

using namespace std;
using namespace std::chrono;


pair<uint64_t, int32_t> bestFromRange(
    uint64_t start, uint64_t end, int32_t size, int32_t capacity, const int32_t weights[], const int32_t values[])
{
    uint64_t best = 0;
    int32_t bestValue = 0;
    
    for (uint64_t i = start, max = end; i < max; ++i) {
        int32_t weight = 0;
        int32_t value = 0;
        
        for (int32_t j = 0; j < size; ++j) {
            uint64_t positionUsed = ((i >> j) & 1);
            if (!positionUsed)
                continue;
            weight += weights[j];
            value += values[j];
        }
        if (weight <= capacity && value > bestValue) {
            best = i;
            bestValue = value;
        }
    }
    
    return make_pair(best, bestValue);
}


int32_t main()
{
    auto startTime = steady_clock::now();
    
    const int32_t size = 32;
    const int32_t capacity = 75;
    const int32_t weights[] = {3, 1, 6, 10, 1, 4, 9, 1, 7, 2, 6, 1, 6, 2, 2, 4, 8, 1, 7, 3, 6, 2, 9, 5, 3, 3, 4, 7, 3, 5, 30, 50};
    const int32_t values[] = {7, 4, 9, 18, 9, 15, 4, 2, 6, 13, 18, 12, 12, 16, 19, 19, 10, 16, 14, 3, 14, 4, 15, 7, 5, 10, 10, 13, 19, 9, 8, 5};
    
    uint64_t best = 0;
    int32_t bestValue = 0;
    
    uint64_t start = 0;
    uint64_t end = pow(2., size);
    
    
    const int32_t asyncCalls = 16;
    vector<future<pair<uint64_t , int32_t>>> futures;
    futures.reserve(asyncCalls);
    
    for (int32_t i = 0; i < asyncCalls; ++i) {
        uint64_t s = end * i / asyncCalls;
        uint64_t e = end * (i+1) / asyncCalls;
        
        futures.push_back(
            async(launch::async, bestFromRange, s, e, size, capacity, weights, values)
        );
    }
    
    for (auto& future : futures) {
        auto results = future.get();
        
        if (results.second > bestValue) {
            best = results.first;
            bestValue = results.second;
        }
    }

    cout << "best permutation: " << bitset<size>(best) << endl;
    cout << "best value: " << bestValue << endl;
    
    cout << "elapsed time: " << duration_cast<seconds>(steady_clock::now() - startTime).count() << endl;
}

//    const int32_t size = 7;
//    int32_t capacity = 15;
//    int32_t weights[] = {2, 3, 5, 7, 1, 4, 1};
//    int32_t values[] = {10, 5, 15, 7, 6, 18, 3};
//    const int32_t size = 10;
//    int32_t capacity = 165;
//    int32_t weights[] = {23,
//                     31,
//                     29,
//                     44,
//                     53,
//                     38,
//                     63,
//                     85,
//                     89,
//                     82};
//    int32_t values[] = {92,
//                    57,
//                    49,
//                    68,
//                    60,
//                    43,
//                    67,
//                    84,
//                    87,
//                    72};

