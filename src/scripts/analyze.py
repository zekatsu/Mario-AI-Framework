import numpy as np
import pandas as pd

# %%
df = pd.read_csv("gameEvents.csv", header=None)
df.head()
# %%
for i in range(df.shape[0] - 1):
    if df.iat[i, 0] > df.iat[i + 1, 0]:
        print(df.iloc[i])
# %%
